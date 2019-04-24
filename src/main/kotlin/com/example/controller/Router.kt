package com.example.controller

import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.*
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.router

@Component
class Router(val appointmentHandler: AppointmentHandler) {

    @Bean
    fun build() =
            router {
                "/api".nest {
                    accept(APPLICATION_JSON).nest {
                        GET("/appointments", appointmentHandler::findByDate)
                        GET("/appointments/{id}", appointmentHandler::findById)
                        POST("/appointments", appointmentHandler::create)
                        PUT("/appointments/{id}", appointmentHandler::update)
                        DELETE("/appointments/{id}", appointmentHandler::delete)
                    }
                }
            }
}

