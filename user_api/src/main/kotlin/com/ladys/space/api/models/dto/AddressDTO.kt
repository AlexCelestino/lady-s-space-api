package com.ladys.space.api.models.dto

data class AddressDTO(
        val zipCode: String?,
        val address: String?,
        val neighbourhood: String?,
        val city: String?,
        val state: String?
)