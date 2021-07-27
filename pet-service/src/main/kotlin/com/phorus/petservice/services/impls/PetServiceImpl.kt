package com.phorus.petservice.services.impls

import com.phorus.petservice.exceptions.ResourceNotFoundException
import com.phorus.petservice.model.dbentities.Pet
import com.phorus.petservice.model.dtos.PetDTO
import com.phorus.petservice.repositories.PetRepository
import com.phorus.petservice.services.PetService
import org.springframework.stereotype.Service


@Service
class PetServiceImpl(
    private val petRepository: PetRepository,
) : PetService {

    override suspend fun findById(petId: Long): Pet =
        petRepository.findById(petId)
            ?: throw ResourceNotFoundException("Pet not found with id: $petId")

    override suspend fun create(pet: Pet): Long =
        petRepository.save(pet).id as Long

    override suspend fun update(petId: Long, petDTO: PetDTO) {
        val pet = petRepository.findById(petId)
            ?: throw ResourceNotFoundException("Pet not found with id: $petId")

        pet.apply {
            petDTO.name?.let { this.name = it }
            petDTO.age?.let { this.age = it }
            petDTO.userId?.let { this.userId = it }
        }

        petRepository.save(pet)
    }

    override suspend fun deleteById(petId: Long) =
        petRepository.deleteById(petId)

    override suspend fun getAmountOfPets(userId: Long): Long =
        petRepository.countByUserId(userId)
}
