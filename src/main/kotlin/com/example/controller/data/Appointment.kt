package com.example.controller.data

import java.time.LocalDateTime
import javax.validation.constraints.Email
import javax.validation.constraints.Future
import javax.validation.constraints.NotNull

data class Appointment (
    val date: LocalDateTime? = null,

    val name: String? = null,

    val email: String? = null,

    val private: Boolean = false,

    val doctorId: Long? = null
)
