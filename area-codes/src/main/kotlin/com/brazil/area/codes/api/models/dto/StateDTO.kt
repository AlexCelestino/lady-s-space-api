package com.brazil.area.codes.api.models.dto

import java.io.Serializable

data class StateDTO(
        var state: String,
        var federativeUnit: String,
        var areaCodes: List<String>
) : Serializable