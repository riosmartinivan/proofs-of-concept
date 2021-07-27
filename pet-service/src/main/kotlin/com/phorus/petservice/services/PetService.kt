package com.phorus.petservice.services

import com.phorus.petservice.model.dbentities.Pet
import com.phorus.petservice.model.dtos.PetDTO

interface PetService {

    suspend fun findById(petId: Long): Pet

    suspend fun create(pet: Pet): Long

    suspend fun update(petId: Long, petDTO: PetDTO)

    suspend fun deleteById(petId: Long)

    suspend fun getAmountOfPets(userId: Long): Long
}