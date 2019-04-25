package com.example.appointments.stub

import com.example.service.EmailService
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Primary
@Profile("test")
class TestEmailService : EmailService {

    val details = mutableListOf<String>()

    override fun send(to: String, subject: String, body: String) {
        details.add(to)
        details.add(subject)
        details.add(body)
    }
}