package com.ladys.space.api.models.dto

import java.io.Serializable
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class PasswordDTO(
        @field:Size(min = 6, message = "{user.invalid.password.length}")
        @field:NotEmpty(message = "{register.null.password}")
        var password: String
) : Serializable