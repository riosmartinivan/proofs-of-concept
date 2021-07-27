package com.phorus.userservice.services.impls;

import com.phorus.userservice.exceptions.ResourceNotFoundException
import com.phorus.userservice.model.dbentities.User
import com.phorus.userservice.model.dtos.UserDTO
import com.phorus.userservice.repositories.UserRepository
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
class UserServiceImplTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var petService: PetServiceImpl

    @InjectMocks
    lateinit var userService: UserServiceImpl

    @Test
    fun findById(): Unit = runBlocking {
        userRepository.stub {
            onBlocking { it.findById(eq(1L)) } doReturn createUser()
        }

        val response: User = userService.findById(1L)

        assertEquals(1L, response.id)
    }

    @Test
    fun `findById - user not found`(): Unit = runBlocking {
        userRepository.stub {
            onBlocking { it.findById(eq(1L)) } doReturn null
        }

        try {
            userService.findById(1L)
            fail("Exception not thrown")
        } catch (ex: ResourceNotFoundException) {
            assertEquals("User not found with id: 1", ex.message)
        }
    }

    @Test
    fun create(): Unit = runBlocking {
        userRepository.stub {
            onBlocking { it.save(any<User>()) } doReturn createUser()
        }

        val response: Long = userService.create(createUser())

        assertEquals(1L, response)

        verify(userRepository, times(1)).save(any<User>())
    }

    @Test
    fun update(): Unit = runBlocking {
        userRepository.stub {
            onBlocking { it.findById(eq(1L)) } doReturn createUser()
        }

        userService.update(1L, createUserDto())

        val userArgumentCaptor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository, times(1)).save(userArgumentCaptor.capture())

        assertEquals("test2", userArgumentCaptor.value.name)
        assertEquals(22, userArgumentCaptor.value.age)
    }

    @Test
    fun `update - only name`(): Unit = runBlocking {
        userRepository.stub {
            onBlocking { it.findById(eq(1L)) } doReturn createUser()
        }

        userService.update(1L, UserDTO("test2"))

        val userArgumentCaptor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository, times(1)).save(userArgumentCaptor.capture())

        assertEquals("test2", userArgumentCaptor.value.name)
        assertEquals(21, userArgumentCaptor.value.age)
    }

    @Test
    fun `update - user not found`(): Unit = runBlocking {
        userRepository.stub {
            onBlocking { it.findById(eq(1L)) } doReturn null
        }

        try {
            userService.update(1L, createUserDto())
            fail("Exception not thrown")
        } catch (ex: ResourceNotFoundException) {
            assertEquals("User not found with id: 1", ex.message)
        }

        verify(userRepository, times(0)).save(any<User>())
    }

    @Test
    fun deleteById(): Unit = runBlocking {
        userService.deleteById(1L)

        verify(userRepository, times(1)).deleteById(eq(1L))
    }


    @Test
    fun getAmountOfUsers(): Unit = runBlocking {
        petService.stub {
            onBlocking { it.getAmountOfPets(eq(1L)) } doReturn 5L
        }

        val response: Long = userService.getAmountOfPets(1L)

        assertEquals(5L, response)
    }


    private fun createUser(): User =
        User(1, "test", 21)

    private fun createUserDto(): UserDTO =
        UserDTO("test2", 22)
}