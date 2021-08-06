package com.phorus.gateway.routes

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
internal class RoutesConfigurationTest(
    @Autowired var webTestClient: WebTestClient
) {
    private val wireMockServer = WireMockServer(9091)

    @BeforeAll
    fun setup() = wireMockServer.start()

    @AfterAll
    fun teardown() = wireMockServer.stop()

    @Test
    fun `test dynamic routing`(): Unit = runBlocking {
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo("/pets/getAmountOfPets?userId=1"))
                .willReturn(
                    WireMock.aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200).withBody("3")
                )
        )

        webTestClient.get()
            .uri { it.path("/api/pet-service/pets/getAmountOfPets")
                .queryParam("userId", 1L)
                .build()}
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .json("3")

    }
}