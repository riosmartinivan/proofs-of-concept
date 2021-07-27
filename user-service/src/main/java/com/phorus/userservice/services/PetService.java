package com.phorus.userservice.services;

import reactor.core.publisher.Mono;

public interface PetService {

    Mono<Long> getAmountOfPets(Long userId);
}
