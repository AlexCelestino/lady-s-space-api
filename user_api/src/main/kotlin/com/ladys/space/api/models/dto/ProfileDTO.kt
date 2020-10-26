package com.ladys.space.api.models.dto

import java.io.Serializable
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ProfileDTO(
        @field:NotBlank(message = "{register.null.name}")
        var name: String,

        @field:NotBlank(message = "{register.null.last-name}")
        var lastName: String,

        @field:Size(max = 1, message = "{invalid.gender}")
        @field:NotEmpty(message = "{register.null.gender}")
        var gender: String,

        @field:NotNull(message = "{register.null.birth-bate}")
        var birthDate: LocalDate,

        @field:NotEmpty(message = "{register.null.zip-code}")
        @field:Size(min = 8, message = "{user.invalid.zip-code}")
        var zipCode: String,

        @field:NotBlank(message = "{register.null.address}")
        var address: String,

        @field:NotBlank(message = "{register.null.neighbourhood}")
        var neighbourhood: String,

        @field:NotBlank(message = "{register.null.city}")
        var city: String,

        @field:NotBlank(message = "{register.null.state}")
        var state: String
) : Serializable