package com.phorus.userservice.repositories

import com.phorus.userservice.model.dbentities.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository: CoroutineCrudRepository<User, Long>
