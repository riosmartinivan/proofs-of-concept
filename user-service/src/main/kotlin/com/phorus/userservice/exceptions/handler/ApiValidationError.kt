package com.phorus.userservice.exceptions.handler

data class ApiValidationError(
    val obj: String,
    val field: String? = null,
    val rejectedValue: Any? = null,
    val message: String? = null,
) : ApiSubError()