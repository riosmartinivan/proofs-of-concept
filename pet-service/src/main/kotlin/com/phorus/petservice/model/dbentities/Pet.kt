package com.phorus.petservice.model.dbentities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("pets")
class Pet(
    @Id
    var id: Long? = null,
    var name: String? = null,
    var age: Int? = null,
    var userId: Long? = null, // Owner
)