package com.example.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Appointment(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        val date: LocalDateTime,

        val name: String,

        val private: Boolean,

        @ManyToOne
        val patient: Patient,

        @ManyToOne
        val doctor: Doctor
)