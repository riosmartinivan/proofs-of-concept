package com.phorus.petservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phorus.petservice.model.dbentities.Pet;


@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    long countByUserId(Long userId);
}
