package com.phorus.userservice.services;

import com.phorus.userservice.model.dbentities.User;
import com.phorus.userservice.model.dtos.UserDTO;
import reactor.core.publisher.Mono;


public interface UserService {

    Mono<User> findById(Long userId);

    Mono<Long> create(Mono<User> userMono);

    Mono<Void> update(Long userId, Mono<UserDTO> userDTOMono);

    Mono<Void> deleteById(Long userId);


    Mono<Long> getAmountOfPets(Long userId);
}
