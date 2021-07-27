package com.phorus.userservice.controllers;

import com.phorus.userservice.mappings.toEntity
import com.phorus.userservice.mappings.toResponse
import com.phorus.userservice.model.dtos.UserDTO
import com.phorus.userservice.model.dtos.UserResponse
import com.phorus.userservice.model.validationGroups.UserCreateValGroup
import com.phorus.userservice.model.validationGroups.UserUpdateValGroup
import com.phorus.userservice.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI


@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/{userId}")
    suspend fun get(@PathVariable userId: Long): ResponseEntity<UserResponse> =
        userService.findById(userId)
            .toResponse()
            .let { ResponseEntity.ok(it) }

    @PostMapping
    suspend fun create(
        @Validated(UserCreateValGroup::class)
        @RequestBody
        userDTO: UserDTO
    ): ResponseEntity<Long> =
        userDTO.toEntity()
            .let { userService.create(it) }
            .let { ResponseEntity.created(URI.create("/users/$it")).build() }

    @PutMapping
    suspend fun update(
        @RequestParam("userId")
        userId: Long,

        @Validated(UserUpdateValGroup::class)
        @RequestBody
        userDTO: UserDTO
    ): ResponseEntity<Void> =
        userService.update(userId, userDTO)
            .let { ResponseEntity.ok().build() }

    @DeleteMapping("/{userId}")
    suspend fun delete(@PathVariable("userId") userId: Long): ResponseEntity<Void> =
        userService.deleteById(userId)
            .let { ResponseEntity.ok().build() }


    @GetMapping("/getAmountOfPets")
    suspend fun getAmountOfPets(@RequestParam("userId") userId: Long): ResponseEntity<Long> =
        userService.getAmountOfPets(userId)
            .let { ResponseEntity.ok(it) }
}
