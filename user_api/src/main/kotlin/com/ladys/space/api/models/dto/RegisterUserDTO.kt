package com.ladys.space.api.models.dto

import java.io.Serializable
import javax.validation.constraints.NotNull

data class RegisterUserDTO(

        @field:NotNull(message = "{register.null.user}")
        val user: UserDTO?,

        @field:NotNull(message = "{register.null.address}")
        val address: AddressDTO?,

        @field:NotNull(message = "{register.null.contact}")
        val contact: ContactDTO?

) : Serializable