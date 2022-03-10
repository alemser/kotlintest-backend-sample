package com.example.appointments

import com.example.ExampleApplication
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.row
import io.kotest.data.forAll
import io.restassured.RestAssured
import io.restassured.RestAssured.*
import org.hamcrest.Matchers.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@SpringBootTest(classes = [ExampleApplication::class], webEnvironment = RANDOM_PORT)
class AppointmentGetIT(@LocalServerPort port: String) : StringSpec() {

    init {
        RestAssured.port = port.toInt()
        baseURI = "http://localhost"

        "should get appointments by id" {
            forAll (
                   row(1, -1,200, "Robert Smith", true, "John Smith"),
                   row(99, 0, 404, null, null, null),
                   row(1, 0, 304, null, null, null)
            ) { id, version, expStatus, expName, expType, expDocName ->

                val response =
                given()
                        .basePath("/api")
                        .contentType("application/json")
                        .header("If-None-Match", "\"$version\"")
                .`when`()
                        .get("/appointments/$id")
                .then()
                        .statusCode(expStatus)

                if (expStatus == 200) {
                    response
                            .header("ETag", equalTo("\"0\""))
                            .body("name", equalTo(expName))
                            .body("doctor_name", equalTo(expDocName))
                            .body("private", equalTo(expType))
                }
            }
        }

        "should get appointments by date" {
            val someDate = LocalDateTime.of(2019, 1,1, 1,0,0,0);

            forAll (
                    row(someDate, 200, 3, arrayOf("Robert Smith", "Another patient", "David Bowie")),
                    row(now(), 200, 4, arrayOf("Lou Reed", "Another patient 2", "David Gilmour", "Reger Waters")),
                    row(now().plusDays(1), 200, 0, arrayOf())
            ) { date, expStatus, expNumElements, expNames ->
                given()
                    .basePath("/api")
                    .contentType("application/json")
                .`when`()
                    .get("/appointments?date=$date")
                .then()
                    .statusCode(expStatus)


                val response =
                        given()
                                .basePath("/api")
                                .contentType("application/json")
                            .`when`()
                                .get("/appointments?date=$date")
                            .then()
                                .statusCode(expStatus)

                if (expStatus == 200) {
                    response
                            .body("size", equalTo(expNumElements))
                            .body("name", containsInAnyOrder(*expNames))
                }
            }
        }
    }
}
