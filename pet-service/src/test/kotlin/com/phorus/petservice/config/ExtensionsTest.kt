package com.phorus.petservice.config

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ExtensionsTest {

    @Test
    fun `map from one object to another`() {
        val tmpVar = Tmp(23, "nameTest", "surnameTest", 87)

        val result = tmpVar.mapTo(TmpDTO::class)

        val expectedResult = TmpDTO(surname = "surnameTest")
        assertEquals(expectedResult, result)
    }

    @Test
    fun `map from one object to another ignoring a property`() {
        val tmpVar = Tmp(23, "nameTest", "surnameTest", 87)

        val result = tmpVar.mapTo(TmpDTO::class, listOf("surname"))

        val expectedResult = TmpDTO()
        assertEquals(expectedResult, result)
    }

    @Test
    fun `map from one object to another manually mapping a property`() {
        val tmpVar = Tmp(23, "nameTest", "surnameTest", 87)

        val result = tmpVar.mapTo(TmpDTO::class, mappings = hashMapOf("name" to "nameStr"))

        val expectedResult = TmpDTO("nameTest", surname = "surnameTest")
        assertEquals(expectedResult, result)
    }

    @Test
    fun `map from one object to another manually parsing a property with a function`() {
        val tmpVar = Tmp(23, "nameTest", "surnameTest", 87)

        val parseToStringManually : (Int) -> String = {
            (it + 5).toString()
        }

        val result = tmpVar.mapTo(
            TmpDTO::class, customMappings = hashMapOf("age"
                to Pair("ageStr", parseToStringManually)))

        val expectedResult = TmpDTO(ageStr = "92", surname = "surnameTest")
        assertEquals(expectedResult, result)
    }

    @Test
    fun `map from one object to another with manual parsing with and without a function, while ignoring one property`() {
        val tmpVar = Tmp(23, "nameTest", "surnameTest", 87)

        val parseToStringManually : (Int) -> String = {
            (it + 5).toString()
        }

        val result = tmpVar.mapTo(
            TmpDTO::class, listOf("surname"), hashMapOf("name" to "nameStr"),
            hashMapOf("age" to Pair("ageStr", parseToStringManually)))

        val expectedResult = TmpDTO("nameTest", "92")
        assertEquals(expectedResult, result)
    }


    // A nullable and a non nullable property are treated as equal in the mapper,
    //  so name should be mapped to nameStr without problems
    data class Tmp(
        var id: Long? = null,
        var name: String,
        var surname: String? = null,
        var age: Int? = null
    )

    // The target class must have a no args constructor and all the properties that want to be mapped need to be var
    data class TmpDTO(
        var nameStr: String? = null,
        var ageStr: String? = null,
        var surname: String? = null
    )
}