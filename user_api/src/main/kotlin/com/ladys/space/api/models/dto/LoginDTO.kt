package com.ladys.space.api.models.dto

import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class LoginDTO(

        @field:NotBlank(message = "{login.null.login}")
        @field:Email(message = "{user.invalid.email}")
        val email: String,

        @field:NotBlank(message = "{login.null.password}")
        val password: String

) : Serializable