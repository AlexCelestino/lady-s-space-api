package com.ladys.space.api.services.helpers

import java.util.*

class ConverterHelper {
    companion object {
        fun toBase64(value: String): String = Base64.getEncoder().encodeToString(value.toByteArray())

        fun base64ToString(value: String): String = String(Base64.getDecoder().decode(value))
    }
}