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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public ResponseEntity<UserResponse> get(@PathVariable Long userId) {

        return ResponseEntity.ok(
                userMapper.userToUserResponse(
                        userService.findById(userId)
                )
        );
    }

    @PostMapping
    public ResponseEntity<Long> create(@Validated(UserValidationGroup.UserCreateValGroup.class)
                                           @RequestBody UserDTO userDTO) {

        Long userId = userService.create(userMapper.userDtoToUser(userDTO));

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(userId).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestParam("userId") Long userId,
                                       @Validated(UserValidationGroup.UserUpdateValGroup.class)
                                       @RequestBody UserDTO userDTO) {

        userService.update(userId, userDTO);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {

        userService.deleteById(userId);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/getAmountOfPets")
    public ResponseEntity<Long> getAmountOfPets(@RequestParam("userId") Long userId) {

        return ResponseEntity.ok(userService.getAmountOfPets(userId));
    }
}
