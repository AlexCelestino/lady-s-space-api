package com.ladys.space.api.errors

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ApiErrors(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
        var time: LocalDateTime = LocalDateTime.now(),
        var status: Int,
        var error: HttpStatus,
        var messages: List<String>
)

