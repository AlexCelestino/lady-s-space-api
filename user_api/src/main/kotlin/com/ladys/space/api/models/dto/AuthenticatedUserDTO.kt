package com.ladys.space.api.models.dto

import java.io.Serializable
import java.time.LocalDate

data class AuthenticatedUserDTO(
        val name: String,
        val lastName: String,
        val email: String,
        val dateBirth: LocalDate,
        val address: String,
        val token: String,
        val expirationDate: String
) : Serializable