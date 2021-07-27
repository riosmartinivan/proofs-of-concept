package com.phorus.petservice.exceptions.handler

import com.phorus.petservice.exceptions.BadRequestException
import com.phorus.petservice.exceptions.ResourceNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException

import javax.validation.ConstraintViolationException


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(WebExchangeBindException::class)
    protected fun handleMethodArgumentNotValid(ex: WebExchangeBindException): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST, "Validation error")
        apiError.addValidationErrors(ex.bindingResult.fieldErrors)
        apiError.addValidationError(ex.bindingResult.globalErrors)
        return buildResponseEntity(apiError)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST, "Validation error")
        apiError.addValidationErrors(ex.constraintViolations)
        return buildResponseEntity(apiError)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    protected fun handleNotFoundExceptions(ex: RuntimeException): ResponseEntity<Any> =
        buildResponseEntity(ApiError(HttpStatus.NOT_FOUND, ex.message))

    @ExceptionHandler(BadRequestException::class)
    protected fun handleBadRequestExceptions(ex: RuntimeException): ResponseEntity<Any> =
        buildResponseEntity(ApiError(HttpStatus.BAD_REQUEST, ex.message))


    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any> =
        ResponseEntity(apiError, apiError.status)
}
