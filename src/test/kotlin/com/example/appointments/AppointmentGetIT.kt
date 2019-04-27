package com.example.appointments

import com.example.Application
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import io.kotlintest.data.forall
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@SpringBootTest(classes = [Application::class], webEnvironment = RANDOM_PORT)
class AppointmentGetIT(@LocalServerPort port: String) : StringSpec() {

    init {
        RestAssured.port = port.toInt()
        RestAssured.baseURI = "http://localhost"

        "should get appointments by id" {
            forall (
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

            forall (
                    row(someDate, 200, 3, arrayOf("Robert Smith", "Another patient", "David Bowie")),
                    row(now(), 200, 4, arrayOf("Lou Reed", "Another patient 2", "David Gilmour", "Reger Waters")),
                    row(now().plusDays(1), 200, 0, arrayOf())
            ) { date, expStatus, expNumElements, expNames ->

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
