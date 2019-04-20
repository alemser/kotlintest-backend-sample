package com.example.appointments

import com.example.Application
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import io.kotlintest.data.forall
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

@SpringBootTest(classes = [Application::class], webEnvironment = RANDOM_PORT)
class AppointmentGet(flyway: Flyway,
                     @Value("\${local.server.port}") port: String) : StringSpec() {

    init {
        RestAssured.port = port.toInt()
        RestAssured.baseURI = "http://localhost"

        "should get appointments by id" {
            forall (
                   row(1, 200, "Patient 1", true, "John Smith")
            ) { id, expectedStatus, patientName, isPrivate, doctorName ->

                given()
                        .basePath("/api")
                        .contentType("application/json")
                        .expect()
                        .statusCode(expectedStatus)
                        .`when`()
                        .get("/appointments/$id")
                        .then()


            }
        }

        flyway.clean()
        flyway.migrate()
    }

}