package com.brazil.area.codes.api.controllers

import com.brazil.area.codes.api.errors.ApiError
import com.brazil.area.codes.api.errors.exceptions.ResourceNotFoundException
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AdviceController {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun resourceNotFoundExceptionHandler(exception: ResourceNotFoundException): ResponseEntity<ApiError> =
            ResponseEntity.status(NOT_FOUND).body(ApiError(details = exception.message ?: ""))

}