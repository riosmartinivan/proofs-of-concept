package com.phorus.userservice.model.dtos

import com.phorus.userservice.model.validationGroups.UserCreateValGroup
import com.phorus.userservice.model.validationGroups.UserUpdateValGroup

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserDTO(
    @field:NotBlank(groups = [UserCreateValGroup::class], message = "Cannot be blank")
    @field:Size(
        groups = [UserCreateValGroup::class, UserUpdateValGroup::class],
        min = 3,
        message = "Must have at least 3 characters long"
    )
    var name: String? = null,

    @field:NotNull(groups = [UserCreateValGroup::class], message = "Cannot be null")
    @field:Min(
        groups = [UserCreateValGroup::class, UserUpdateValGroup::class],
        value = 0,
        message = "Cannot be negative"
    )
    var age: Int? = null,
)

data class UserResponse(
    val id: Long,
    val name: String,
    val age: Int,
)