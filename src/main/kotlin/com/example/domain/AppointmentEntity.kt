package com.example.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "appointment")
data class AppointmentEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        val date: LocalDateTime,

        val name: String,

        val private: Boolean,

        @ManyToOne(cascade = [CascadeType.REFRESH])
        val doctor: DoctorEntity,

        @Version
        val version: Long
)
