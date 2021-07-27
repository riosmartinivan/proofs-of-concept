package com.phorus.userservice.exceptions.handler

import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import com.fasterxml.jackson.annotation.JsonTypeInfo

class LowerCaseClassNameResolver: TypeIdResolverBase() {

    override fun idFromValue(value: Any): String = value.javaClass.simpleName.lowercase()

    override fun idFromValueAndType(value: Any, suggestedType: Class<*>?): String = idFromValue(value)

    override fun getMechanism(): JsonTypeInfo.Id = JsonTypeInfo.Id.CUSTOM
}