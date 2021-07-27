package com.phorus.petservice.services;

import com.phorus.petservice.model.dbentities.Pet;
import com.phorus.petservice.model.dtos.PetDTO;


public interface PetService {

    Pet findById(Long petId);

    Long create(Pet pet);

    void update(Long petId, PetDTO petDTO);

    void deleteById(Long petId);


    Long getAmountOfPets(Long userId);
}
