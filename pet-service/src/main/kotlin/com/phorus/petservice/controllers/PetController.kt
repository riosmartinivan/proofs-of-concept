package com.phorus.petservice.controllers

import com.phorus.petservice.mappings.toEntity
import com.phorus.petservice.mappings.toResponse
import com.phorus.petservice.model.dtos.PetDTO
import com.phorus.petservice.model.dtos.PetResponse
import com.phorus.petservice.model.validationGroups.PetCreateValGroup
import com.phorus.petservice.model.validationGroups.PetUpdateValGroup
import com.phorus.petservice.services.PetService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI


@RestController
@RequestMapping("/pets")
class PetController(
    private val petService: PetService,
) {

    @GetMapping("/{petId}")
    suspend fun get(@PathVariable petId: Long): ResponseEntity<PetResponse> =
        petService.findById(petId)
            .toResponse()
            .let { ResponseEntity.ok(it) }

    @PostMapping
    suspend fun create(
        @Validated(PetCreateValGroup::class)
        @RequestBody
        petDTO: PetDTO
    ): ResponseEntity<Long> =
        petDTO.toEntity()
            .let { petService.create(it) }
            .let { ResponseEntity.created(URI.create("/pets/$it")).build() }

    @PutMapping
    suspend fun update(
        @RequestParam("petId")
        petId: Long,

        @Validated(PetUpdateValGroup::class)
        @RequestBody
        petDTO: PetDTO
    ): ResponseEntity<Void> =
        petService.update(petId, petDTO)
            .let { ResponseEntity.ok().build() }

    @DeleteMapping("/{petId}")
    suspend fun delete(@PathVariable("petId") petId: Long): ResponseEntity<Void> =
        petService.deleteById(petId)
            .let { ResponseEntity.ok().build() }


    @GetMapping("/getAmountOfPets")
    suspend fun getAmountOfPets(@RequestParam("userId") userId: Long): ResponseEntity<Long> =
        petService.getAmountOfPets(userId)
            .let { ResponseEntity.ok(it) }
}
