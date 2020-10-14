package com.ladys.space.api.constants

class ApiConstants private constructor() {
    object CrossOriginConfig {
        const val TEST: String = "http://localhost:8081/occurrences-api"
        const val DEV: String = "https://occurrences-hdiyc.herokuapp.com/occurrences-api"
    }

    object Headers {
        const val OCCURRENCES_HEADER: String = "occurrencesToken"
    }
}