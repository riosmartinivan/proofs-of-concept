package com.phorus.petservice.services.impls;

import com.phorus.petservice.exceptions.ResourceNotFoundException;
import com.phorus.petservice.model.dbentities.Pet;
import com.phorus.petservice.model.dtos.PetDTO;
import com.phorus.petservice.repositories.PetRepository;
import com.phorus.petservice.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Mono<Pet> findById(Long petId) {
        return petRepository.findById(petId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Pet not found")));
    }

    @Override
    public Mono<Long> create(Mono<Pet> petMono) {
        return petMono
                .flatMap(petRepository::save)
                .map(Pet::getId);
    }

    @Override
    public Mono<Void> update(Long petId, Mono<PetDTO> petDTOMono) {
        return petRepository.findById(petId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Pet not found")))
                .zipWith(petDTOMono, (pet, petDTO) -> {
                    if (petDTO.getName() != null && !petDTO.getName().isEmpty()) {
                        pet.setName(petDTO.getName());
                    }
                    if (petDTO.getAge() != null) {
                        pet.setAge(petDTO.getAge());
                    }
                    if (petDTO.getUserId() != null) {
                        pet.setUserId(petDTO.getUserId());
                    }

                    return pet;
                })
                .doOnNext(petRepository::save)
                .then();
    }

    @Override
    public Mono<Void> deleteById(Long petId) {
        return petRepository.deleteById(petId);
    }


    @Override
    public Mono<Long> getAmountOfPets(Long userId) {
        return petRepository.countByUserId(userId);
    }
}
