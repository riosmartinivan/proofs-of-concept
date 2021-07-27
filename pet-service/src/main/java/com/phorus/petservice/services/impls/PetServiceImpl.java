package com.phorus.petservice.services.impls;

import com.phorus.petservice.exceptions.ResourceNotFoundException;
import com.phorus.petservice.model.dbentities.Pet;
import com.phorus.petservice.model.dtos.PetDTO;
import com.phorus.petservice.repositories.PetRepository;
import com.phorus.petservice.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Pet findById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));
    }

    @Override
    public Long create(Pet pet) {
        return petRepository.save(pet).getId();
    }

    @Override
    public void update(Long petId, PetDTO petDTO) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));

        if (petDTO.getName() != null && !petDTO.getName().isEmpty()) {
            pet.setName(petDTO.getName());
        }

        if (petDTO.getAge() != null) {
            pet.setAge(petDTO.getAge());
        }

        petRepository.save(pet);
    }

    @Override
    public void deleteById(Long petId) {
        petRepository.deleteById(petId);
    }


    @Override
    public Long getAmountOfPets(Long userId) {

        return petRepository.countByUserId(userId);
    }
}
