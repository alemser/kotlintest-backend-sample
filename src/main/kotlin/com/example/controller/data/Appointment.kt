package com.example.controller.data

import java.time.LocalDateTime
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank

data class Appointment (
    @Future
    val date: LocalDateTime,

    @NotBlank
    val name: String,

    @NotBlank
    val email: String,

    val private: Boolean = false,

    val doctorId: Long
)
