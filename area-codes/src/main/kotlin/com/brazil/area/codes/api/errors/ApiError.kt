package com.brazil.area.codes.api.errors

data class ApiError(val message: String = "Validation failed.", val details: String)