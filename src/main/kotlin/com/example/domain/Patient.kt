package com.example.domain

import javax.persistence.*

@Entity
data class Patient(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        val name: String,

        @ManyToOne
        @JoinColumn(name = "doctor_id")
        val preferredDoctor: Doctor
)
