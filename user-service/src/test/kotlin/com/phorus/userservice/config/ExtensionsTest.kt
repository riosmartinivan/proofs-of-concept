package com.phorus.userservice.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class ExtensionsTest {

    @Test
    fun `map from one object to another`() {
        val tmpVar = Tmp(23, "nameTest", "surnameTest", 87)

        val result = tmpVar.mapTo(TmpDTO::class)

        assertEquals("surnameTest", result.surname)
    }

    @Test
    fun `map from one object to another ignoring a property`() {
        val tmpVar = Tmp(23, "nameTest", "surnameTest", 87)

        val result = tmpVar.mapTo(TmpDTO::class, listOf("surname"))

        assertNull(result.nameStr)
        assertNull(result.ageStr)
        assertNull(result.surname)
    }

    @Test
    fun `map from one object to another manually mapping a property`() {
        val tmpVar = Tmp(23, "nameTest", "surnameTest", 87)

        val result = tmpVar.mapTo(TmpDTO::class, mappings = hashMapOf("name" to "nameStr"))

        assertEquals("nameTest", result.nameStr)
        assertEquals("surnameTest", result.surname)
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

        assertEquals("92", result.ageStr)
        assertEquals("surnameTest", result.surname)
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

        assertEquals("nameTest", result.nameStr)
        assertEquals("92", result.ageStr)
    }

    @Test
    fun `map from one compound object to another`() {
        val tmpVar = TmpCompound(Tmp(23, "nameTest", "surnameTest"), "testProp")

        val result = tmpVar.mapTo(TmpCompoundDTO::class)

        assertEquals("testProp", result.compoundTestProp)
        assertEquals("surnameTest", result.compoundTmp?.surname)
    }

    @Test
    fun `map from one super compound object to another`() {
        val tmpVar = TmpSuperCompound(
            TmpCompound(Tmp(23, "nameTest", "surnameTest"), "testProp"),
            12,
        )

        val result = tmpVar.mapTo(TmpSuperCompoundDTO::class)

        assertEquals(12, result.superCompoundTestProp)
        assertEquals("testProp", result.superCompoundTmp?.compoundTestProp)
        assertEquals("surnameTest", result.superCompoundTmp?.compoundTmp?.surname)
    }

    @Test
    fun `map from one super compound object to another with a function`() {
        val tmpVar = TmpSuperCompound(
            TmpCompound(Tmp(23, "nameTest", "surnameTest"), "testProp"),
            12,
        )

        val addFiveToInt : (Int) -> Int = {
            it + 5
        }

        val result = tmpVar.mapTo(TmpSuperCompoundDTO::class,
            customMappings = hashMapOf("superCompoundTestProp" to Pair("superCompoundTestProp", addFiveToInt)))

        assertEquals(17, result.superCompoundTestProp)
        assertEquals("testProp", result.superCompoundTmp?.compoundTestProp)
        assertEquals("surnameTest", result.superCompoundTmp?.compoundTmp?.surname)
    }


    // A nullable and a non nullable property are treated as equal in the mapper,
    //  so name should be mapped to nameStr without problems
    class Tmp(
        var id: Long? = null,
        var name: String,
        var surname: String? = null,
        var age: Int? = null
    )

    // The target class must have a no args constructor and all the properties that want to be mapped need to be var
    class TmpDTO(
        var nameStr: String? = null,
        var ageStr: String? = null,
        var surname: String? = null
    )

    class TmpCompound(
        var compoundTmp: Tmp? = null,
        var compoundTestProp: String? = null,
    )

    class TmpCompoundDTO(
        var compoundTmp: TmpDTO? = null,
        var compoundTestProp: String? = null,
    )

    class TmpSuperCompound(
        var superCompoundTmp: TmpCompound? = null,
        var superCompoundTestProp: Int? = null,
    )

    class TmpSuperCompoundDTO(
        var superCompoundTmp: TmpCompoundDTO? = null,
        var superCompoundTestProp: Int? = null,
    )
}