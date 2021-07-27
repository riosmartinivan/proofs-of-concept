package com.phorus.userservice.services.impls;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.phorus.userservice.exceptions.BadRequestException;
import com.phorus.userservice.exceptions.ResourceNotFoundException;
import com.phorus.userservice.services.PetService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PetServiceImplTest {

    private final PetService petService;

    private WireMockServer wireMockServer;

    @Autowired
    public PetServiceImplTest(PetService petService) {
        this.petService = petService;
    }

    @BeforeEach
    public void setup () {
        wireMockServer = new WireMockServer(9091);
        wireMockServer.start();
    }

    @AfterEach
    public void teardown () {
        wireMockServer.stop();
    }

    @Test
    void getAmountOfPets() {
        wireMockServer.stubFor(get(urlEqualTo("/pets/getAmountOfPets?userId=1"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200).withBody("3")));

        Mono<Long> response = petService.getAmountOfPets(1L);

        StepVerifier.create(response)
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    void getAmountOfPets_not_found() {
        wireMockServer.stubFor(get(urlEqualTo("/pets/getAmountOfPets?userId=1"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(404)));

        Mono<Long> response = petService.getAmountOfPets(1L);

        StepVerifier.create(response)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void getAmountOfPets_bad_request() {
        wireMockServer.stubFor(get(urlEqualTo("/pets/getAmountOfPets?userId=1"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(500)));

        Mono<Long> response = petService.getAmountOfPets(1L);

        StepVerifier.create(response)
                .expectError(BadRequestException.class)
                .verify();
    }
}