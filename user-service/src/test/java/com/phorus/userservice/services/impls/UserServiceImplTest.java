package com.phorus.userservice.services.impls;

import com.phorus.userservice.exceptions.ResourceNotFoundException;
import com.phorus.userservice.mappers.UserMapper;
import com.phorus.userservice.model.dbentities.User;
import com.phorus.userservice.model.dtos.UserDTO;
import com.phorus.userservice.repositories.UserRepository;
import com.phorus.userservice.services.PetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PetService petService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findById() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(createUser()));

        User response = userService.findById(1L);

        assertEquals(response, createUser());
    }

    @Test
    void findById_not_found() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.empty());

        try {
            userService.findById(1L);
            fail("Exception not thrown");
        } catch (ResourceNotFoundException ex) {
            assertEquals("User not found with id: 1", ex.getMessage());
        }
    }

    @Test
    void create() {
        when(userRepository.save(any(User.class))).thenReturn(createUser());

        Long response = userService.create(createUser());

        assertEquals(1L, response);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void update() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(createUser()));

        userService.update(1L, createUserDto());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        assertEquals("pepe2", userArgumentCaptor.getValue().getName());
        assertEquals(6, userArgumentCaptor.getValue().getAge());
    }

    @Test
    void update_only_name() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(createUser()));

        userService.update(1L, UserDTO.builder().name("pepe2").build());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        assertEquals("pepe2", userArgumentCaptor.getValue().getName());
        assertEquals(5, userArgumentCaptor.getValue().getAge());
    }

    @Test
    void update_not_found() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.empty());

        try {
            userService.update(1L, createUserDto());
            fail("Exception not thrown");
        } catch (ResourceNotFoundException ex) {
            assertEquals("User not found with id: 1", ex.getMessage());
        }

        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void deleteById() {
        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(eq(1L));
    }


    @Test
    void getAmountOfPets() {
        when(petService.getAmountOfPets(eq(1L))).thenReturn(3L);

        Long response = userService.getAmountOfPets(1L);

        assertEquals(3L, response);
    }


    private User createUser() {
        return User.builder()
                .id(1L)
                .name("pepe")
                .age(5)
                .build();
    }

    private UserDTO createUserDto() {
        return UserDTO.builder()
                .name("pepe2")
                .age(6)
                .build();
    }
}