package com.phorus.userservice.repositories;

import com.phorus.userservice.model.dbentities.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
