package com.phorus.userservice.services

import com.phorus.userservice.model.dbentities.User
import com.phorus.userservice.model.dtos.UserDTO
import reactor.core.publisher.Mono

interface UserService {

    suspend fun findById(userId: Long): User

    suspend fun create(user: User): Long

    suspend fun update(userId: Long, userDTO: UserDTO)

    suspend fun deleteById(userId: Long)

    suspend fun getAmountOfPets(userId: Long): Long
}