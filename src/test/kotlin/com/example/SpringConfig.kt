package com.example

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import javax.sql.DataSource


@org.springframework.context.annotation.Configuration
class SpringConfig {
    @Bean
    fun getDataSource(): DataSource? {
        val dbPort = pgContainer.getMappedPort(5432)
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName("org.postgresql.Driver")
        dataSourceBuilder.url("jdbc:postgresql://localhost:$dbPort/test?stringtype=unspecified")
        dataSourceBuilder.username("test")
        dataSourceBuilder.password("test")
        return dataSourceBuilder.build()
    }
}