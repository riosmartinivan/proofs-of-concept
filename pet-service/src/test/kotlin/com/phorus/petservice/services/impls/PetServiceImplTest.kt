package com.phorus.petservice.services.impls

import com.phorus.petservice.exceptions.ResourceNotFoundException
import com.phorus.petservice.model.dbentities.Pet
import com.phorus.petservice.model.dtos.PetDTO
import com.phorus.petservice.repositories.PetRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.fail


@ExtendWith(MockitoExtension::class)
class PetServiceImplTest {

    @Mock
    lateinit var petRepository: PetRepository

    @InjectMocks
    lateinit var petService: PetServiceImpl

    @Test
    fun findById(): Unit = runBlocking {
        petRepository.stub {
            onBlocking { it.findById(eq(1L)) } doReturn createPet()
        }

        val response: Pet = petService.findById(1L)

        assertEquals(1L, response.id)
    }

    @Test
    fun `findById - pet not found`(): Unit = runBlocking {
        petRepository.stub {
            onBlocking { it.findById(eq(1L)) } doReturn null
        }

        try {
            petService.findById(1L)
            fail("Exception not thrown")
        } catch (ex: ResourceNotFoundException) {
            assertEquals("Pet not found with id: 1", ex.message)
        }
    }

    @Test
    fun create(): Unit = runBlocking {
        petRepository.stub {
            onBlocking { it.save(any<Pet>()) } doReturn createPet()
        }

        val response: Long = petService.create(createPet())

        assertEquals(1L, response)

        verify(petRepository, times(1)).save(any<Pet>())
    }

    @Test
    fun update(): Unit = runBlocking {
        petRepository.stub {
            onBlocking { it.findById(eq(1L)) } doReturn createPet()
        }

        petService.update(1L, createPetDto())

        val petArgumentCaptor = ArgumentCaptor.forClass(Pet::class.java)
        verify(petRepository, times(1)).save(petArgumentCaptor.capture())

        assertEquals("test2", petArgumentCaptor.value.name)
        assertEquals(22, petArgumentCaptor.value.age)
        assertEquals(2L, petArgumentCaptor.value.userId)
    }

    @Test
    fun `update - only name`(): Unit = runBlocking {
        petRepository.stub {
            onBlocking { it.findById(eq(1L)) } doReturn createPet()
        }

        petService.update(1L, PetDTO("test2"))

        val petArgumentCaptor = ArgumentCaptor.forClass(Pet::class.java)
        verify(petRepository, times(1)).save(petArgumentCaptor.capture())

        assertEquals("test2", petArgumentCaptor.value.name)
        assertEquals(21, petArgumentCaptor.value.age)
        assertEquals(1L, petArgumentCaptor.value.userId)
    }

    @Test
    fun `update - pet not found`(): Unit = runBlocking {
        petRepository.stub {
            onBlocking { it.findById(eq(1L)) } doReturn null
        }

        try {
            petService.update(1L, createPetDto())
            fail("Exception not thrown")
        } catch (ex: ResourceNotFoundException) {
            assertEquals("Pet not found with id: 1", ex.message)
        }

        verify(petRepository, times(0)).save(any<Pet>())
    }

    @Test
    fun deleteById(): Unit = runBlocking {
        petService.deleteById(1L)

        verify(petRepository, times(1)).deleteById(eq(1L))
    }


    @Test
    fun getAmountOfPets(): Unit = runBlocking {
        petRepository.stub {
            onBlocking { it.countByUserId(eq(1L)) } doReturn 5L
        }

        val response: Long = petService.getAmountOfPets(1L)

        assertEquals(5L, response)
    }


    private fun createPet(): Pet =
        Pet(1, "test", 21, 1L)

    private fun createPetDto(): PetDTO =
        PetDTO("test2", 22, 2L)
}