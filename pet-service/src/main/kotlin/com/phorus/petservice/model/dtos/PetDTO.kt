package com.phorus.petservice.model.dtos

import com.phorus.petservice.model.validationGroups.PetCreateValGroup
import com.phorus.petservice.model.validationGroups.PetUpdateValGroup

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class PetDTO(
    @field:NotBlank(groups = [PetCreateValGroup::class], message = "Cannot be blank")
    @field:Size(
        groups = [PetCreateValGroup::class, PetUpdateValGroup::class],
        min = 3,
        message = "Must have at least 3 characters long"
    )
    var name: String? = null,

    @field:NotNull(groups = [PetCreateValGroup::class], message = "Cannot be null")
    @field:Min(
        groups = [PetCreateValGroup::class, PetUpdateValGroup::class],
        value = 0,
        message = "Cannot be negative"
    )
    var age: Int? = null,

    @field:NotNull(groups = [PetCreateValGroup::class], message = "Cannot be null")
    var userId: Long? = null,
)

data class PetResponse(
    var id: Long? = null,
    var name: String? = null,
    var age: Int? = null,
    var userId: Long? = null,
)