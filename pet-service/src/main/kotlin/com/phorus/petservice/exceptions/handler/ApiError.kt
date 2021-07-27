package com.phorus.petservice.exceptions.handler

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.time.LocalDateTime
import javax.validation.ConstraintViolation

@JsonTypeInfo(
    include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM,
    property = "error", visible = true
)
@JsonTypeIdResolver(LowerCaseClassNameResolver::class)
data class ApiError(
    val status: HttpStatus,
    val message: String? = null,
    val debugMessage: String? = null,
) {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now()
    var subErrors: MutableList<ApiSubError>? = null

    fun addSubError(subError: ApiSubError) {
        if (subErrors == null) subErrors = mutableListOf()
        subErrors!!.add(subError)
    }

    fun addValidationError(obj: String, message: String?, field: String? = null, rejectedValue: Any? = null) =
        addSubError(ApiValidationError(obj, field, rejectedValue, message))

    fun addValidationError(fieldError: FieldError) =
        addValidationError(
            fieldError.objectName,
            fieldError.defaultMessage,
            fieldError.field,
            fieldError.rejectedValue,
        )

    fun addValidationErrors(fieldErrors: List<FieldError>) =
        fieldErrors.forEach(this::addValidationError)

    fun addValidationError(objectError: ObjectError) =
        addValidationError(
            objectError.objectName,
            objectError.defaultMessage
        )

    fun addValidationError(globalErrors: List<ObjectError>) =
        globalErrors.forEach { addValidationError(it) }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    fun addValidationError(cv: ConstraintViolation<*>) =
        addValidationError(
            cv.rootBeanClass.simpleName,
            cv.message,
            (cv.propertyPath as PathImpl).leafNode.asString(),
            cv.invalidValue,
        )

    fun addValidationErrors(constraintViolations: Set<ConstraintViolation<*>>) =
        constraintViolations.forEach(this::addValidationError)
}
