package com.ladys.space.api.models.dto

data class ContactDTO(
        val personalContact: PersonalContactDTO?,
        var emergencyContacts: List<EmergencyContactDTO>?
)
