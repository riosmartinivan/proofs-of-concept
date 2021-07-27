package com.phorus.userservice.model.dtos

import com.phorus.userservice.model.validationGroups.UserCreateValGroup
import com.phorus.userservice.model.validationGroups.UserUpdateValGroup
import org.junit.jupiter.api.Test
import javax.validation.Validation
import javax.validation.Validator
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class UserDTOTest {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `test update validations with errors`() {
        val userDTO = UserDTO("ab", -1)

        val violations = validator.validate(userDTO, UserUpdateValGroup::class.java)

        assertEquals(2, violations.size)

        violations.forEach {
            when (it.propertyPath.toString()) {
                "name" -> assertEquals("Must have at least 3 characters long", it.message)
                "age" -> assertEquals("Cannot be negative", it.message)
                else -> fail("Unknown violation")
            }
        }
    }

    @Test
    fun `test update validations without errors`() {
        val userDTO = UserDTO()

        val violations = validator.validate(userDTO, UserUpdateValGroup::class.java)

        assertEquals(0, violations.size)
    }

    @Test
    fun `test create validations with errors`() {
        val userDTO = UserDTO("")

        val violations = validator.validate(userDTO, UserCreateValGroup::class.java)

        assertEquals(3, violations.size)

        violations.forEach {
            when (it.propertyPath.toString()) {
                "name" -> assertTrue(
                    it.message == "Cannot be blank" ||
                            it.message == "Must have at least 3 characters long",
                    "Unexpected name violation value: " + it.message
                )
                "age" -> assertEquals("Cannot be null", it.message)
                else -> fail("Unknown violation")
            }
        }
    }

    @Test
    fun `test create validations without errors`() {
        val userDTO = UserDTO("abc", 1)

        val violations = validator.validate(userDTO, UserCreateValGroup::class.java)

        assertEquals(0, violations.size)
    }
}