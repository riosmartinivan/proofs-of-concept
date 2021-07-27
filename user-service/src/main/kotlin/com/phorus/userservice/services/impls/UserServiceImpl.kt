package com.phorus.userservice.services.impls;

import com.phorus.userservice.exceptions.ResourceNotFoundException
import com.phorus.userservice.model.dbentities.User
import com.phorus.userservice.model.dtos.UserDTO
import com.phorus.userservice.repositories.UserRepository
import com.phorus.userservice.services.PetService
import com.phorus.userservice.services.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val petService: PetService,
) : UserService {

    override suspend fun findById(userId: Long): User =
        userRepository.findById(userId)
            ?: throw ResourceNotFoundException("User not found with id: $userId")

    override suspend fun create(user: User): Long =
        userRepository.save(user).id as Long

    override suspend fun update(userId: Long, userDTO: UserDTO) {
        val user = userRepository.findById(userId)
            ?: throw ResourceNotFoundException("User not found with id: $userId")

        user.apply {
            userDTO.name?.let { this.name = it }
            userDTO.age?.let { this.age = it }
        }

        userRepository.save(user)
    }

    override suspend fun deleteById(userId: Long) =
        userRepository.deleteById(userId)

    override suspend fun getAmountOfPets(userId: Long): Long =
        petService.getAmountOfPets(userId)
}
