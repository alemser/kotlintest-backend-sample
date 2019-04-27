package com.example.controller

import com.example.controller.data.Appointment
import com.example.controller.data.asEntity
import com.example.controller.data.asQueryResponse
import com.example.repository.AppointmentRepository
import com.example.service.EmailService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import java.util.concurrent.CompletableFuture.supplyAsync
import org.springframework.web.util.UriComponentsBuilder.fromPath
import java.time.LocalDateTime
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Component
class AppointmentHandler(val repository: AppointmentRepository,
                         val emailService: EmailService,
                         val validator: Validator) {

    fun create(req: ServerRequest) =
            req.bodyToMono(Appointment::class.java)
                    .map { it.asEntity() }
                    .flatMap {
                        deferAsFuture { repository.saveAndFlush(it) }
                    }
                    .doOnSuccess {
                        emailService.send(it.email!!, "Appointment confirmation",
                                "Your appointment is confirmed to ${it.date}")

                    }
                    .flatMap {
                        val location = fromPath("/appointments/{id}").buildAndExpand(it.id).toUri()
                        created(location)
                                .eTag(it.version.toString())
                                .build()
                    }

    fun update(req: ServerRequest) = req.bodyToMono(Appointment::class.java)
            .map {
                val id = req.pathVariable("id").toLong()
                val version = req.headers().header("If-Match")[0].toLong()
                it.asEntity(id, version)
            }.flatMap {
                deferAsFuture { repository.save(it) }
            }.flatMap {
                ok().eTag(it.version.toString()).syncBody(it)
            }

    fun delete(req: ServerRequest) = Mono.just(req.pathVariable("id"))
            .flatMap {
                deferAsFuture { repository.deleteById(it.toLong()) }
            }.flatMap {
                ok().build()
            }

    fun findById(req: ServerRequest) = Mono
            .just(req.pathVariable("id").toLong())
            .flatMap {
                deferAsFuture { repository.findById(it) }
            }.flatMap {
                it.map { e -> Mono.just(e) }.orElseGet { Mono.empty() }
            }
            .flatMap {
                ok().eTag(it.version.toString()).syncBody(it.asQueryResponse())
            }
            .switchIfEmpty { notFound().build() }

    fun findByDate(req: ServerRequest) = Mono
            .just(LocalDateTime.parse(req.queryParam("date").get()))
            .flatMap {
                deferAsFuture { repository.findByDate(it) }
            }
            .flatMap {
                ok().syncBody( it.map { a -> a.asQueryResponse() } )
            }

    fun <T> deferAsFuture(fn: () -> T): Mono<T> = Mono.defer {
            Mono.fromFuture { supplyAsync(fn) }
        }
}