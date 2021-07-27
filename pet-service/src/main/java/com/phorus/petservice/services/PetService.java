package com.phorus.petservice.services;

import com.phorus.petservice.model.dbentities.Pet;
import com.phorus.petservice.model.dtos.PetDTO;
import reactor.core.publisher.Mono;


public interface PetService {

    Mono<Pet> findById(Long petId);

    Mono<Long> create(Mono<Pet> petMono);

    Mono<Void> update(Long petId, Mono<PetDTO> petDTOMono);

    Mono<Void> deleteById(Long petId);


    Mono<Long> getAmountOfPets(Long userId);
}
