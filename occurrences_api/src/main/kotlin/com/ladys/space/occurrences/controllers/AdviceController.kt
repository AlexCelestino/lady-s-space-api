package com.ladys.space.occurrences.controllers

import com.ladys.space.occurrences.errors.ApiErrors
import com.ladys.space.occurrences.errors.exceptions.InvalidAccessException
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException.Forbidden
import java.net.ConnectException

@RestControllerAdvice
class AdviceController {

    @ExceptionHandler(ConnectException::class)
    fun handler(exception: ConnectException): ResponseEntity<ApiErrors> {
        val apiErrors = ApiErrors(
                status = INTERNAL_SERVER_ERROR.value(),
                error = INTERNAL_SERVER_ERROR,
                message = exception.message!!
        )
        return status(INTERNAL_SERVER_ERROR).body(apiErrors)
    }

    @ExceptionHandler(InvalidAccessException::class, Forbidden::class)
    fun handler(exception: InvalidAccessException): ResponseEntity<ApiErrors> {
        val apiErrors = ApiErrors(
                status = FORBIDDEN.value(),
                error = FORBIDDEN,
                message = exception.message!!
        )
        return status(FORBIDDEN).body(apiErrors)
    }

}