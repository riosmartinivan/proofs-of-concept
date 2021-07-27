package com.phorus.userservice.repositories;

import com.phorus.userservice.model.dbentities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
