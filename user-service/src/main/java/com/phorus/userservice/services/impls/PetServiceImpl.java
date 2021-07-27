package com.phorus.userservice.services.impls;

import com.phorus.userservice.config.CustomProperties;
import com.phorus.userservice.exceptions.BadRequestException;
import com.phorus.userservice.exceptions.ResourceNotFoundException;
import com.phorus.userservice.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Class only used for communicating with pet-service
 */
@Service
public class PetServiceImpl implements PetService {

    private final WebClient webClient;

    @Autowired
    public PetServiceImpl(WebClient.Builder builder, CustomProperties customProperties) {
        this.webClient = builder.baseUrl(customProperties.getPetServiceUrl()).build();
    }

    @Override
    public Mono<Long> getAmountOfPets(Long userId) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pets/getAmountOfPets")
                        .queryParam("userId", userId)
                        .build())
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND, response ->
                        Mono.error(new ResourceNotFoundException("User not found")))
                .onStatus(status -> status != HttpStatus.OK, response ->
                        Mono.error(new BadRequestException("Error while trying to send a request to pet-service")))
                .bodyToMono(Long.class)
                .map(response -> response);
    }
}
