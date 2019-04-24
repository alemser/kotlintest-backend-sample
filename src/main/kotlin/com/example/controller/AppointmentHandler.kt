package com.example.controller

import com.example.controller.data.Appointment
import com.example.controller.data.asEntity
import com.example.controller.data.asQueryResponse
import com.example.domain.AppointmentEntity
import com.example.repository.AppointmentRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.Executors
import java.util.function.Supplier
import org.springframework.web.util.UriComponentsBuilder.fromPath
import java.time.LocalDateTime

@Component
class AppointmentHandler(val repository: AppointmentRepository) {

    private val executor = Executors.newWorkStealingPool()

    fun create(req: ServerRequest) = req.bodyToMono(AppointmentEntity::class.java)
            .flatMap {
                deferAsFuture { repository.save(it) }
            }.flatMap {
                val location = fromPath("/appointments/{id}").buildAndExpand(it.id).toUri()
                created(location)
                        .eTag(it.version.toString())
                        .syncBody(it)
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