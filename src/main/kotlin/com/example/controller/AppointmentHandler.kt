package com.example.controller

import com.example.domain.Appointment
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

@Component
class AppointmentHandler(val repository: AppointmentRepository) {

    private val executor = Executors.newWorkStealingPool()

    fun create(req: ServerRequest) = req.bodyToMono(Appointment::class.java)
            .flatMap {
                Mono.fromFuture {
                    supplyAsync( Supplier { repository.save(it) }, executor)
                }
            }
            .flatMap {
                val location = fromPath("/appointments/{id}").buildAndExpand(it.id).toUri()
                created(location).syncBody(it)
            }

    fun findById(req: ServerRequest) = Mono
            .just(req.pathVariable("id").toLong())
            .flatMap {
                Mono.fromFuture {
                    supplyAsync( Supplier { repository.findById(it) }, executor)
                }
            }
            .flatMap { ok().syncBody(it) }
            .switchIfEmpty { notFound().build() }

}