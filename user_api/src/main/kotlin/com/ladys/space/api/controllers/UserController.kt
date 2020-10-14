package com.ladys.space.api.controllers

import com.ladys.space.api.models.dto.RegisterUserDTO
import com.ladys.space.api.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody registerUserDTO: RegisterUserDTO, locale: Locale): ResponseEntity<Void> {
        this.userService.registerUser(registerUserDTO, locale)
        return status(CREATED).build()
    }

}