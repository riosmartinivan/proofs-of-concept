package com.phorus.userservice.mappings;

import com.phorus.userservice.model.dbentities.User
import com.phorus.userservice.model.dtos.UserDTO
import com.phorus.userservice.model.dtos.UserResponse


fun User.toResponse(): UserResponse = UserResponse(this.id as Long, this.name, this.age)
fun UserDTO.toEntity(): User = User(name = this.name as String, age = this.age as Int)