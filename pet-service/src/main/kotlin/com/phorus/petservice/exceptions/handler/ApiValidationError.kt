package com.phorus.petservice.exceptions.handler

data class ApiValidationError(
    val obj: String,
    val field: String? = null,
    val rejectedValue: Any? = null,
    val message: String? = null,
) : ApiSubError()