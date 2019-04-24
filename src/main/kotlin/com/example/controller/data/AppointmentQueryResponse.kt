package com.example.controller.data

import java.time.LocalDateTime

class AppointmentQueryResponse (

    val id: Long,
    val date: LocalDateTime,
    val name: String,
    val private: Boolean,
    val doctorName: String

)
