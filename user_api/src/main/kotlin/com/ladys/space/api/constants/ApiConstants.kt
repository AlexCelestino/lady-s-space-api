package com.ladys.space.api.constants

class ApiConstants private constructor() {
    object CrossOriginConfig {
        const val OCCURRENCES_API: String = "https://ladys-space-user-api.herokuapp.com/api"
    }

    object Headers {
        const val OCCURRENCES_HEADER: String = "occurrencesToken "
        const val TOKEN: String = "Bearer "
    }
}