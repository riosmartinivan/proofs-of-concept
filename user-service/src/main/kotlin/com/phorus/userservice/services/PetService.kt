package com.phorus.userservice.services

interface PetService {

    suspend fun getAmountOfPets(userId: Long): Long
}