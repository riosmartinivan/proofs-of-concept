package com.phorus.petservice.mappings

import com.phorus.petservice.model.dbentities.Pet
import com.phorus.petservice.model.dtos.PetDTO
import com.phorus.petservice.model.dtos.PetResponse


fun Pet.toResponse(): PetResponse = PetResponse(this.id as Long, this.name, this.age, this.userId)
fun PetDTO.toEntity(): Pet = Pet(name = this.name as String, age = this.age as Int, userId = this.userId as Long)