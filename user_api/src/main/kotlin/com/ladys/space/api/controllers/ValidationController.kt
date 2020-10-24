package com.ladys.space.api.controllers

import com.ladys.space.api.constants.ApiConstants.CrossOriginConfig.OCCURRENCES_API
import com.ladys.space.api.models.UserModel
import com.ladys.space.api.services.ValidationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/validate")
class ValidationController {

    @Autowired
    private lateinit var validationService: ValidationService

    @GetMapping("/token")
    @CrossOrigin(methods = [RequestMethod.GET], origins = [OCCURRENCES_API])
    fun validateToken(): ResponseEntity<Unit> = status(ACCEPTED).build()

    @GetMapping("email/{email}")
    fun validateEmail(@PathVariable("email") email: String): ResponseEntity<UserModel> {
        this.validationService.validateEmail(email)
        return ResponseEntity.noContent().build()
    }

}