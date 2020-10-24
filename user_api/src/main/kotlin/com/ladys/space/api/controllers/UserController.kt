package com.ladys.space.api.controllers

import com.ladys.space.api.models.UserModel
import com.ladys.space.api.models.dto.*
import com.ladys.space.api.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping
    fun getUser(@RequestHeader(name = AUTHORIZATION, required = true) token: String): ResponseEntity<UserModel> =
            ok(this.userService.getUser(token))

    @PostMapping("/login")
    fun authentication(@Valid @RequestBody loginDTO: LoginDTO): ResponseEntity<AuthenticatedUserDTO> =
            status(HttpStatus.ACCEPTED).body(this.userService.authenticateUser(loginDTO))

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody registerDTO: RegisterDTO): ResponseEntity<Unit> {
        this.userService.registerUser(registerDTO)
        return status(CREATED).build()
    }

    @PutMapping("/update")
    fun updateUser(@Valid @RequestBody profileDTO: ProfileDTO): ResponseEntity<UserModel> {
        return ok(this.userService.updateUser(profileDTO))
    }

    @PatchMapping("/change-password")
    fun changePassword(
            @RequestHeader(name = AUTHORIZATION, required = true) token: String,
            @Valid @RequestBody passwordDTO: PasswordDTO
    ): ResponseEntity<Unit> {
        this.userService.updatePassword(token, passwordDTO)
        return ok().build()
    }

}