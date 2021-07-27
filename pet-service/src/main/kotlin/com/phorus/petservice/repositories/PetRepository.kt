package com.phorus.petservice.repositories

import com.phorus.petservice.model.dbentities.Pet
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PetRepository : CoroutineCrudRepository<Pet, Long> {

    suspend fun countByUserId(userId: Long): Long
}