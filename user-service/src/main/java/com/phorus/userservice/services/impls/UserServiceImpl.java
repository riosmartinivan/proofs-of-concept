package com.phorus.userservice.services.impls;

import com.phorus.userservice.exceptions.ResourceNotFoundException;
import com.phorus.userservice.model.dbentities.User;
import com.phorus.userservice.model.dtos.UserDTO;
import com.phorus.userservice.repositories.UserRepository;
import com.phorus.userservice.services.PetService;
import com.phorus.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Override
    public Long create(User user) {
        return userRepository.save(user).getId();
    }

    @Override
    public void update(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
            user.setName(userDTO.getName());
        }

        if (userDTO.getAge() != null) {
            user.setAge(userDTO.getAge());
        }

        userRepository.save(user);
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }


    @Override
    public Long getAmountOfPets(Long userId) {
        return petService.getAmountOfPets(userId);
    }
}
