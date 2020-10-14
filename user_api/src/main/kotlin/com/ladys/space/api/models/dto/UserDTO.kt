package com.ladys.space.api.models.dto

import java.time.LocalDate

data class UserDTO(
        val name: String?,
        val lastName: String?,
        val email: String?,
        val birthDate: LocalDate?,
        val gender: Char?,
        val password: String?
)