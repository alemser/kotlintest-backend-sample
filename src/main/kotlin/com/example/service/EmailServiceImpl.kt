package com.example.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

interface EmailService {
    fun send(to: String, subject: String, body: String)
}

@Service
class EmailServiceImpl : EmailService {
    private val logger = LoggerFactory.getLogger(EmailServiceImpl::class.java)

    override fun send(to: String, subject: String, body: String) {
        logger.info("Using e-mail API to send email to $to")
    }
}