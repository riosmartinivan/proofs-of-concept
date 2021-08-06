package com.phorus.userservice.services.impls;

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.phorus.userservice.exceptions.BadRequestException
import com.phorus.userservice.exceptions.ResourceNotFoundException
import com.phorus.userservice.services.PetService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
import kotlin.test.fail

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
internal class PetServiceImplTest(
    @Autowired var petService: PetService,
) {

    private val wireMockServer = WireMockServer(9091)

    @BeforeAll
    fun setup() = wireMockServer.start()

    @AfterAll
    fun teardown() = wireMockServer.stop()

    @Test
    fun `get number of pets of a user`(): Unit = runBlocking {
        wireMockServer.stubFor(
            get(urlEqualTo("/pets/getAmountOfPets?userId=1"))
                .willReturn(
                    aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200).withBody("3")
                )
        )

        val response: Long = petService.getAmountOfPets(1L)

        assertEquals(3L, response)
    }

    @Test
    fun `get number of pets of a user - user not found`(): Unit = runBlocking {
        wireMockServer.stubFor(
            get(urlEqualTo("/pets/getAmountOfPets?userId=1"))
                .willReturn(
                    aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(404)
                )
        )

        try {
            petService.getAmountOfPets(1L)
            fail("Exception not thrown")
        } catch (ex: ResourceNotFoundException) {
            assertEquals("User not found with id: 1", ex.message)
        }
    }

    @Test
    fun `get number of pets of a user - 400 error`(): Unit = runBlocking {
        wireMockServer.stubFor(
            get(urlEqualTo("/pets/getAmountOfPets?userId=1"))
                .willReturn(
                    aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(400)
                )
        );

        try {
            petService.getAmountOfPets(1L)
            fail("Exception not thrown")
        } catch (ex: BadRequestException) {
            assertEquals("Error", ex.message)
        }
    }
}