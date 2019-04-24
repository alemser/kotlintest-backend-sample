package com.example.controller.data

import com.example.domain.AppointmentEntity
import com.example.domain.DoctorEntity

fun AppointmentEntity.asQueryResponse() =
        AppointmentQueryResponse(
            id = this.id,
            date = this.date,
            name = this.name,
            private = this.private,
            doctorName = this.doctor.name
        )

fun Appointment.asEntity(givenId: Long, givenVersion: Long) =
        AppointmentEntity(
                id = givenId,
                version = givenVersion,
                date = this.date,
                name = this.name,
                private = this.private,
                doctor = DoctorEntity(this.doctorId, "", 0)
        )