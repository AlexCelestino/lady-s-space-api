package com.ladys.space.api.models.dto

import java.io.Serializable
import javax.validation.constraints.NotBlank

data class LoginDTO(
        @field:NotBlank(message = "{login.null.login}")
        val email: String,

        @field:NotBlank(message = "{login.null.password}")
        val password: String
) : Serializable