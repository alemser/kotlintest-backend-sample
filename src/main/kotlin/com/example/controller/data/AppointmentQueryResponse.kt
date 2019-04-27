package com.example.controller.data

import java.time.LocalDateTime

class AppointmentQueryResponse (

    val id: Long? = null,
    val date: LocalDateTime? = null,
    val name: String? = null,
    val email: String? = null,
    val private: Boolean? = null,
    val doctorName: String? = null

)
