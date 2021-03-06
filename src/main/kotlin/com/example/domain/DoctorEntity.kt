package com.example.domain

import javax.persistence.*

@Entity
@Table(name = "doctor")
data class DoctorEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        val name: String,

        @Version
        val version: Long
)
