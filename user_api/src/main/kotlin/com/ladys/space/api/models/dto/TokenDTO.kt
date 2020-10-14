package com.ladys.space.api.models.dto

import java.io.Serializable

data class TokenDTO(
        val token: String,
        val expirationDate: String
) : Serializable