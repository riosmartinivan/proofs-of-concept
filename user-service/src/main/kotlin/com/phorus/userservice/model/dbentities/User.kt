package com.phorus.userservice.model.dbentities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("users")
class User(
    @Id
    var id: Long? = null,
    var name: String? = null,
    var age: Int? = null,
)