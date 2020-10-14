package com.ladys.space.api.controllers

import com.ladys.space.api.errors.ApiErrors
import com.ladys.space.api.errors.exceptions.BadCredentialsException
import com.ladys.space.api.errors.exceptions.BadValueException
import com.ladys.space.api.errors.exceptions.InvalidTokenException
import com.ladys.space.api.errors.exceptions.UserNotFoundException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors

@RestControllerAdvice
class AdviceController {

    @ExceptionHandler(BadValueException::class, DataIntegrityViolationException::class)
    fun handler(exception: BadValueException): ResponseEntity<ApiErrors> {
        ApiErrors(
                status = BAD_REQUEST.value(),
                error = BAD_REQUEST,
                messages = listOf(exception.message)
        ).also {
            return status(BAD_REQUEST).body(it)
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handler(exception: MethodArgumentNotValidException): ResponseEntity<ApiErrors> {
        val errors: MutableList<String> = exception.bindingResult.allErrors.stream()
                .map { it.defaultMessage }.collect(Collectors.toList())

        ApiErrors(
                status = BAD_REQUEST.value(),
                error = BAD_REQUEST,
                messages = errors
        ).also {
            return status(BAD_REQUEST).body(it)
        }
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handler(exception: BadCredentialsException): ResponseEntity<ApiErrors> {
        ApiErrors(
                status = UNAUTHORIZED.value(),
                error = UNAUTHORIZED,
                messages = listOf(exception.message!!)
        ).also {
            return status(UNAUTHORIZED).body(it)
        }
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handler(exception: UsernameNotFoundException): ResponseEntity<ApiErrors> {
        ApiErrors(
                status = FORBIDDEN.value(),
                error = FORBIDDEN,
                messages = listOf(exception.message!!)
        ).also {
            return status(FORBIDDEN).body(it)
        }
    }

    @ExceptionHandler(InvalidTokenException::class, ExpiredJwtException::class, SignatureException::class)
    fun handler(exception: InvalidTokenException): ResponseEntity<ApiErrors> {
        ApiErrors(
                status = FORBIDDEN.value(),
                error = FORBIDDEN,
                messages = listOf(exception.message!!)
        ).also {
            return status(FORBIDDEN).body(it)
        }
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handler(exception: UserNotFoundException): ResponseEntity<ApiErrors> {
        ApiErrors(
                status = NOT_FOUND.value(),
                error = NOT_FOUND,
                messages = listOf(exception.message!!)
        ).also {
            return status(NOT_FOUND).body(it)
        }
    }

}