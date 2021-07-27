package com.phorus.userservice.controllers;

import com.phorus.userservice.mappers.UserMapper;
import com.phorus.userservice.model.dtos.UserDTO;
import com.phorus.userservice.model.dtos.responses.UserResponse;
import com.phorus.userservice.model.validationGroups.UserValidationGroup;
import com.phorus.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserResponse>> get(@PathVariable Long userId) {

        return userService.findById(userId)
                .map(userMapper::userToUserResponse)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<Long>> create(@Validated(UserValidationGroup.UserCreateValGroup.class)
                                                 @RequestBody Mono<UserDTO> userDTO) {

        return userDTO.map(userMapper::userDtoToUser)
                .flatMap(user -> userService.create(Mono.just(user)))
                .map(userId -> ResponseEntity.created(URI.create("/users/" + userId)).build());
    }

    @PutMapping
    public Mono<ResponseEntity<Void>> update(@RequestParam("userId") Long userId,
                                             @Validated(UserValidationGroup.UserUpdateValGroup.class)
                                             @RequestBody Mono<UserDTO> userDTO) {

        return userService.update(userId, userDTO)
                .flatMap(p -> Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("/{userId}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("userId") Long userId) {

        return userService.deleteById(userId)
                .flatMap(p -> Mono.just(ResponseEntity.ok().build()));
    }


    @GetMapping("/getAmountOfPets")
    public Mono<ResponseEntity<Long>> getAmountOfPets(@RequestParam("userId") Long userId) {

        return userService.getAmountOfPets(userId)
                .flatMap(p -> Mono.just(ResponseEntity.ok(p)));
    }
}
