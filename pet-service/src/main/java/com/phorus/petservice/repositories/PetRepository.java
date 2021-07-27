package com.phorus.petservice.repositories;

import com.phorus.petservice.model.dbentities.Pet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface PetRepository extends ReactiveCrudRepository<Pet, Long> {

    Mono<Long> countByUserId(Long userId);
}
