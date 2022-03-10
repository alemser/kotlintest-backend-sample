package com.example;

import io.kotest.core.config.AbstractProjectConfig
import org.testcontainers.containers.PostgreSQLContainer

var pgContainer = PostgreSQLContainer<Nothing>("postgres:11.1")

object ProjectConfig : AbstractProjectConfig() {

    override suspend fun beforeProject() {
        super.beforeProject()
        System.setProperty("spring.profiles.active", "local,test")
        pgContainer.withExposedPorts(5432, 5432)
        pgContainer.start()
    }

    override suspend fun afterProject() {
        super.afterProject()
        pgContainer.stop()
    }
}
