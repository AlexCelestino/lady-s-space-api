package com.ladys.space.occurrences.constants

class ErrorMessageConstants private constructor() {
    companion object {
        const val INVALID_TOKEN: String = "Invalid token."
        const val USER_UNAUTHORIZED: String = "User not found. Access denied."
        const val MISSING_AUTHORIZATION_HEADER: String = "Missing authorization header."
    }
}