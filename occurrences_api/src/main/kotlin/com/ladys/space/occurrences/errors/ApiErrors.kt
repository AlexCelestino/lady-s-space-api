package com.ladys.space.occurrences.errors

import org.springframework.http.HttpStatus
import java.io.Serializable
import java.time.LocalDateTime

data class ApiErrors(
        val timestamp: LocalDateTime = LocalDateTime.now(),
        val status: String,
        val error: String,
        val message: String
) : Serializable