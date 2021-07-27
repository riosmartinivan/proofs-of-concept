package com.phorus.petservice.model.dbentities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("pets")
class Pet(
    @Id
    var id: Long? = null,
    var name: String,
    var age: Int,
    var userId: Long, // Owner
)