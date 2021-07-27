package com.phorus.petservice.mappers;

import com.phorus.petservice.model.dbentities.Pet;
import com.phorus.petservice.model.dtos.PetDTO;
import com.phorus.petservice.model.dtos.responses.PetResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PetMapper {

    PetResponse petToPetResponse(Pet pet);

    Pet petDtoToPet(PetDTO petDTO);
}
