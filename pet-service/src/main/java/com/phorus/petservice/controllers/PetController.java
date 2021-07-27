package com.phorus.petservice.controllers;

import com.phorus.petservice.mappers.PetMapper;
import com.phorus.petservice.model.dtos.PetDTO;
import com.phorus.petservice.model.dtos.responses.PetResponse;
import com.phorus.petservice.model.validationGroups.PetValidationGroup;
import com.phorus.petservice.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final PetMapper petMapper;

    @Autowired
    public PetController(PetService petService, PetMapper petMapper) {
        this.petService = petService;
        this.petMapper = petMapper;
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetResponse> get(@PathVariable Long petId) {

        return ResponseEntity.ok(
                petMapper.petToPetResponse(
                        petService.findById(petId)
                )
        );
    }

    @PostMapping
    public ResponseEntity<Long> create(@Validated(PetValidationGroup.PetCreateValGroup.class)
                                           @RequestBody PetDTO petDTO) {

        Long petId = petService.create(petMapper.petDtoToPet(petDTO));

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/pets/{petId}")
                .buildAndExpand(petId).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestParam("petId") Long petId,
                                       @Validated(PetValidationGroup.PetUpdateValGroup.class)
                                       @RequestBody PetDTO petDTO) {

        petService.update(petId, petDTO);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> delete(@PathVariable Long petId) {

        petService.deleteById(petId);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/getAmountOfPets")
    public ResponseEntity<Long> getAmountOfPets(@RequestParam("userId") Long userId) {

        return ResponseEntity.ok(petService.getAmountOfPets(userId));
    }
}
