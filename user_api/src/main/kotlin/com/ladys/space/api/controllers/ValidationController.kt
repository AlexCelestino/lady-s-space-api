package com.ladys.space.api.controllers

import com.ladys.space.api.constants.ApiConstants.CrossOriginConfig.DEV
import com.ladys.space.api.models.UserModel
import com.ladys.space.api.services.ValidationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/validate")
class ValidationController {

    @Autowired
    private lateinit var validationService: ValidationService

    @GetMapping("/token")
    @CrossOrigin(methods = [RequestMethod.GET], origins = [DEV])
    fun validateToken(
            @RequestHeader(name = AUTHORIZATION, required = true)
            token: String?,
            locale: Locale): ResponseEntity<Void> {
        this.validationService.validateToken(token, locale)
        return status(HttpStatus.ACCEPTED).build()
    }

    @GetMapping("email/{email}")
    fun validateEmail(@PathVariable("email") email: String, locale: Locale): ResponseEntity<UserModel> {
        this.validationService.validateEmail(email, locale)
        return ResponseEntity.noContent().build()
    }

}