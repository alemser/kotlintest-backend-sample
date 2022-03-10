package com.example.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

/**
 * Primitive catch all exception handling filter.
 */
@Component
@Order(0)
class ExceptionHandler : WebFilter {

    @Autowired
    lateinit var mapper: ObjectMapper

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain) =
        chain
                .filter(exchange)
                .transformDeferred { call -> call.onErrorResume {
                    it.printStackTrace()
                        val response = exchange.response
                        response.statusCode = HttpStatus.BAD_REQUEST
                        response.headers.contentType = MediaType.APPLICATION_JSON
                        val body = mapper.writeValueAsBytes(Error(it.message))
                        val buffer = response.bufferFactory().wrap(body!!)
                        response.writeWith(Mono.just(buffer))
                    }
                }
}

data class Error(val message: String)