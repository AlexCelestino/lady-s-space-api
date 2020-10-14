package com.ladys.space.api.controllers

import com.ladys.space.api.models.dto.LoginDTO
import com.ladys.space.api.models.dto.TokenDTO
import com.ladys.space.api.services.LoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@RestController
class LoginController {

    @Autowired
    private lateinit var loginService: LoginService

    @PostMapping("/login")
    fun authentication(@Valid @RequestBody loginDTO: LoginDTO, locale: Locale): ResponseEntity<TokenDTO> =
            status(ACCEPTED).body(this.loginService.authenticateUser(loginDTO, locale))

}