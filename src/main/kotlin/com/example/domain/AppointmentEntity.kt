package com.example.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "appointment")
data class AppointmentEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        val date: LocalDateTime? = null,

        val name: String? = null,

        val email: String? = null,

        val private: Boolean = false,

        @ManyToOne(cascade = [CascadeType.REFRESH])
        val doctor: DoctorEntity? = null,

        @Version
        val version: Long? = null
)
