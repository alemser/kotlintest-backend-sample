package com.example.repository

import com.example.domain.AppointmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AppointmentRepository : JpaRepository<AppointmentEntity, Long> {

    @Query("SELECT a FROM AppointmentEntity a WHERE DATE(a.date) = DATE(?1)")
    fun findByDate(date: LocalDateTime): List<AppointmentEntity>

}