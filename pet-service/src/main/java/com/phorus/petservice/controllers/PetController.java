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
import reactor.core.publisher.Mono;

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
    public Mono<ResponseEntity<PetResponse>> get(@PathVariable Long petId) {

        return petService.findById(petId)
                .map(petMapper::petToPetResponse)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<Long>> create(
            @Validated(PetValidationGroup.PetCreateValGroup.class)
            @RequestBody Mono<PetDTO> petDTO) {

        return petDTO.map(petMapper::petDtoToPet)
                .flatMap(pet -> petService.create(Mono.just(pet)))
                .map(petId -> ResponseEntity.created(URI.create("/pets/" + petId)).build());
    }

    @PutMapping
    public Mono<ResponseEntity<Void>> update(@RequestParam("petId") Long petId,
                                             @Validated(PetValidationGroup.PetUpdateValGroup.class)
                                             @RequestBody Mono<PetDTO> petDTO) {

        return petService.update(petId, petDTO)
                .flatMap(p -> Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("/{petId}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long petId) {

        return petService.deleteById(petId)
                .flatMap(p -> Mono.just(ResponseEntity.ok().build()));
    }


    @GetMapping("/getAmountOfPets")
    public Mono<ResponseEntity<Long>> getAmountOfPets(@RequestParam("userId") Long userId) {

        return petService.getAmountOfPets(userId)
                .flatMap(p -> Mono.just(ResponseEntity.ok(p)));
    }
}
