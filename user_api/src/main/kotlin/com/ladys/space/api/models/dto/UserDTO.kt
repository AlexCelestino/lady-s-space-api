package com.ladys.space.api.models.dto

import com.ladys.space.api.models.AddressModel
import java.io.Serializable
import java.time.LocalDate

data class UserDTO(
        var name: String,
        var lastName: String,
        var gender: Char,
        var birthDate: LocalDate,
        var address: AddressModel
) : Serializable