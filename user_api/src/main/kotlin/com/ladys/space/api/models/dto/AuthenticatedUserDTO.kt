package com.ladys.space.api.models.dto

import java.io.Serializable

data class AuthenticatedUserDTO(
        val name: String,
        val lastName: String,
        val email: String,
        val address: String,
        val token: String,
        val expirationDate: String
) : Serializable