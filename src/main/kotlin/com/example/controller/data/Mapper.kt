package com.example.controller.data

import com.example.domain.AppointmentEntity
import com.example.domain.DoctorEntity

fun AppointmentEntity.asQueryResponse() =
        AppointmentQueryResponse(
            id = this.id!!,
            date = this.date,
            name = this.name,
            email = this.email,
            private = this.private,
            doctorName = this.doctor?.name
        )

fun Appointment.asEntity(givenId: Long? = null, givenVersion: Long = 0) =
        AppointmentEntity(
                id = givenId,
                version = givenVersion,
                date = this.date,
                name = this.name,
                email = this.email,
                private = this.private,
                doctor = DoctorEntity(this.doctorId, "", 0)
        )