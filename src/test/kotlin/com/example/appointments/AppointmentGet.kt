package com.example.appointments

import com.example.Application
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import io.kotlintest.data.forall
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.flywaydb.core.Flyway
import org.hamcrest.Matchers.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@SpringBootTest(classes = [Application::class], webEnvironment = RANDOM_PORT)
class AppointmentGet(@Value("\${local.server.port}") port: String) : StringSpec() {

    init {
        RestAssured.port = port.toInt()
        RestAssured.baseURI = "http://localhost"

        "should get appointments by id" {
            forall (
                   row(1, -1,200, "Robert Smith", true, "John Smith"),
                   row(99, 0, 404, null, null, null),
                   row(1, 0, 304, null, null, null)
            ) { id, version, expectedStatus, patientName, isPrivate, doctorName ->

                val then =
                given()
                        .basePath("/api")
                        .contentType("application/json")
                        .header("If-None-Match", "\"$version\"")
                .`when`()
                        .get("/appointments/$id")
                .then()
                        .statusCode(expectedStatus)

                if (expectedStatus == 200) {
                    then
                            .header("ETag", equalTo("\"0\""))
                            .body("name", equalTo(patientName))
                            .body("doctor_name", equalTo(doctorName))
                            .body("private", equalTo(isPrivate))
                }
            }
        }

        "should get appointments by date" {
            val someDate = LocalDateTime.of(2019, 1,1, 1,0,0,0);

            forall (
                    row(someDate, 200, 3, arrayOf("Robert Smith", "Another patient", "David Bowie")),
                    row(now(), 200, 4, arrayOf("Lou Reed", "Another patient 2", "David Gilmour", "Reger Waters")),
                    row(now().plusDays(1), 200, 0, arrayOf())
            ) { date, expectedStatus, numRecords, patientNames ->

                val then =
                        given()
                                .basePath("/api")
                                .contentType("application/json")
                                .`when`()
                                .get("/appointments?date=$date")
                                .then()
                                .statusCode(expectedStatus)

                if (expectedStatus == 200) {
                    then
                            .body("size", equalTo(numRecords))
                            .body("name", containsInAnyOrder(*patientNames))
                }
            }
        }
    }
}
