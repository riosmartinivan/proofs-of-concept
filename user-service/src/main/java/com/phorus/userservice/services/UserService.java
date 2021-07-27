package com.phorus.userservice.services;

import com.phorus.userservice.model.dbentities.User;
import com.phorus.userservice.model.dtos.UserDTO;


public interface UserService {

    User findById(Long userId);

    Long create(User user);

    void update(Long userId, UserDTO userDTO);

    void deleteById(Long userId);


    Long getAmountOfPets(Long userId);
}
