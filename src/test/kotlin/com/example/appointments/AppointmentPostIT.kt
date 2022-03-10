package com.example.appointments

import com.example.ExampleApplication
import com.example.controller.data.Appointment
import com.example.repository.AppointmentRepository
import com.example.service.EmailService
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.flywaydb.core.Flyway
import org.hamcrest.Matchers.*
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.PostgreSQLContainer
import java.time.LocalDateTime.now

@SpringBootTest(classes = [ExampleApplication::class], webEnvironment = RANDOM_PORT)
class AppointmentPostIT(val mapper: ObjectMapper,
                        val emailService: EmailService,
                        val appointmentRepository: AppointmentRepository,
                        flyway: Flyway,
                        @LocalServerPort port: String) : StringSpec() {

    init {
        var pgContainer = PostgreSQLContainer<Nothing>("postgres:alpine")
        pgContainer.start()
        RestAssured.port = port.toInt()
        RestAssured.baseURI = "http://localhost"

        val locationIdRegex = """(?<=\/)([^\/]+)${'$'}""".toRegex()

        "should create a new appointment" {
            forAll (
                   row(goodApointment, 201,"", arrayOf("Location", "ETag")),
                   row(badEmailApointment, 400,"constraint [email]", arrayOf()),
                   row(badDoctorApointment, 400,"Unable to find", arrayOf())
            ) { appointment, expStatus, expMessage, expHeaders ->

                val response =
                    given()
                        .basePath("/api")
                        .contentType("application/json")
                        .body( mapper.writeValueAsString(appointment) )
                            .expect()
                            .statusCode(expStatus)
                    .`when`()
                        .post("/appointments")


                if (response.statusCode == 201) {
                    expHeaders.forEach {
                        response
                            .then()
                            .header(it, notNullValue())
                    }

                    response
                            .then()
                            .body(equalTo(expMessage))


                    val match = locationIdRegex.find(response.header("Location"))
                    val id = match?.value
                    val storedAppointment = appointmentRepository.getById(id?.toLong()!!)
                    with(storedAppointment) {
                        date shouldBe appointment.date
                        email shouldBe appointment.email
                        name shouldBe appointment.name
                        doctor?.id shouldBe appointment.doctorId
                        private shouldBe appointment.private
                    }

                } else {
                    response
                            .then()
                            .body(containsString(expMessage))
                }
            }
        }

        "should send an email to patient" {
            given()
                    .basePath("/api")
                    .contentType("application/json")
                    .body( mapper.writeValueAsString(goodApointment) )
                    .`when`()
                    .post("/appointments")
                    .then()
                    .statusCode(201)

            val captorTo = argumentCaptor<String>()
            val captorSubj = argumentCaptor<String>()
            val captorBody = argumentCaptor<String>()
            verify(emailService, atLeastOnce()).send(captorTo.capture(), captorSubj.capture(), captorBody.capture())

            captorTo.firstValue shouldBe goodApointment.email
            captorSubj.firstValue shouldBe "Appointment confirmation"
            captorBody.firstValue shouldBe "Your appointment is confirmed to ${goodApointment.date}"
        }

        flyway.clean()
        flyway.migrate()
    }

    val goodApointment = Appointment(now(), "Ella Fitzgerald", "ella@ella.com", true, 1)
    val badEmailApointment = goodApointment.copy(email = "")
    val badDoctorApointment = goodApointment.copy(doctorId = 99)

    companion object {
        @Configuration
        internal class ContextConfiguration {
            @Bean
            fun emailService(): EmailService = mock()
        }
    }
}
