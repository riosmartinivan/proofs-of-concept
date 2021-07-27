package com.phorus.userservice.services.impls;

import com.phorus.userservice.exceptions.ResourceNotFoundException;
import com.phorus.userservice.model.dbentities.User;
import com.phorus.userservice.model.dtos.UserDTO;
import com.phorus.userservice.repositories.UserRepository;
import com.phorus.userservice.services.PetService;
import com.phorus.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PetService petService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PetService petService) {
        this.userRepository = userRepository;
        this.petService = petService;
    }

    @Override
    public Mono<User> findById(Long userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")));
    }

    @Override
    public Mono<Long> create(Mono<User> userMono) {
        return userMono
                .flatMap(userRepository::save)
                .map(User::getId);
    }

    @Override
    public Mono<Void> update(Long userId, Mono<UserDTO> userDTOMono) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")))
                .zipWith(userDTOMono, (user, userDTO) -> {
                    if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
                        user.setName(userDTO.getName());
                    }
                    if (userDTO.getAge() != null) {
                        user.setAge(userDTO.getAge());
                    }

                    return user;
                })
                .doOnNext(userRepository::save)
                .then();
    }

    @Override
    public Mono<Void> deleteById(Long userId) {
        return userRepository.deleteById(userId);
    }


    @Override
    public Mono<Long> getAmountOfPets(Long userId) {
        return petService.getAmountOfPets(userId);
    }
}
