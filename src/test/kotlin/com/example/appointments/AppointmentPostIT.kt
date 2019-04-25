package com.example.appointments

import com.example.Application
import com.example.appointments.stub.TestEmailService
import com.example.controller.data.Appointment
import com.example.service.EmailService
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import io.kotlintest.data.forall
import io.kotlintest.matchers.string.beBlank
import io.kotlintest.shouldBe
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@SpringBootTest(classes = [Application::class], webEnvironment = RANDOM_PORT)
class AppointmentPostIT(val mapper: ObjectMapper,
                        val emailService: EmailService,
                        @Value("\${local.server.port}") port: String) : StringSpec() {

    init {
        RestAssured.port = port.toInt()
        RestAssured.baseURI = "http://localhost"

        "should create a new appointment" {
            forall (
                   row(goodApointment, 201,"", arrayOf("Location", "ETag")),
                   row(badEmailApointment, 400,"constraint [email]", arrayOf()),
                   row(badDoctorApointment, 400,"Unable to find", arrayOf())
            ) { appointment, expStatus, expMessage, expHeaders ->

                val response =
                    given()
                        .basePath("/api")
                        .contentType("application/json")
                        .body( mapper.writeValueAsString(appointment) )
                    .`when`()
                        .post("/appointments")
                    .then()
                        .statusCode(expStatus)

                if (expStatus == 201) {
                    expHeaders.forEach { response.header(it, notNullValue()) }
                    response.body(equalTo(expMessage))

                } else {
                    response.body(containsString(expMessage))
                }
            }
        }

    }

    val goodApointment = Appointment(now(), "Ella Fitzgerald", "ella@ella.com", true, 1)
    val badEmailApointment = goodApointment.copy(email = "")
    val badDoctorApointment = goodApointment.copy(doctorId = 99)
}
