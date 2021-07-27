package com.phorus.userservice.services.impls;

import com.phorus.userservice.exceptions.ResourceNotFoundException;
import com.phorus.userservice.model.dbentities.User;
import com.phorus.userservice.model.dtos.UserDTO;
import com.phorus.userservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetServiceImpl petService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    public static void setUp() {
        BlockHound.install();
    }

    @Test
    void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            fail("should fail");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @Test
    void findById() {
        when(userRepository.findById(eq(1L))).thenReturn(Mono.just(createUser()));

        Mono<User> response = userService.findById(1L);

        StepVerifier.create(response)
                .expectNext(createUser())
                .verifyComplete();
    }

    @Test
    void findById_not_found() {
        when(userRepository.findById(eq(1L))).thenReturn(Mono.empty());

        Mono<User> response = userService.findById(1L);

        StepVerifier.create(response)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void create() {
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(createUser()));

        Mono<Long> response = userService.create(Mono.just(createUser()));

        StepVerifier.create(response)
                .expectNext(1L)
                .verifyComplete();

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void update() {
        when(userRepository.findById(eq(1L))).thenReturn(Mono.just(createUser()));

        Mono<Void> response = userService.update(1L, Mono.just(createUserDto()));

        StepVerifier.create(response)
                .verifyComplete();

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        assertEquals("test2", userArgumentCaptor.getValue().getName());
        assertEquals(22, userArgumentCaptor.getValue().getAge());
    }

    @Test
    void update_only_name() {
        when(userRepository.findById(eq(1L))).thenReturn(Mono.just(createUser()));

        Mono<Void> response = userService.update(1L, Mono.just(UserDTO.builder()
                .name("test2").build()));

        StepVerifier.create(response)
                .verifyComplete();

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        assertEquals("test2", userArgumentCaptor.getValue().getName());
        assertEquals(21, userArgumentCaptor.getValue().getAge());
    }

    @Test
    void update_not_found() {
        when(userRepository.findById(eq(1L))).thenReturn(Mono.empty());

        Mono<Void> response = userService.update(1L, Mono.just(createUserDto()));

        StepVerifier.create(response)
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void deleteById() {
        when(userRepository.deleteById(eq(1L))).thenReturn(Mono.empty());

        Mono<Void> response = userService.deleteById(1L);

        StepVerifier.create(response)
                .verifyComplete();

        verify(userRepository, times(1)).deleteById(eq(1L));
    }


    @Test
    void getAmountOfUsers() {
        when(petService.getAmountOfPets(eq(1L))).thenReturn(Mono.just(5L));

        Mono<Long> response = userService.getAmountOfPets(1L);

        StepVerifier.create(response)
                .expectNext(5L)
                .verifyComplete();
    }


    private User createUser() {
        return User.builder()
                .id(1L)
                .name("test")
                .age(21)
                .build();
    }

    private UserDTO createUserDto() {
        return UserDTO.builder()
                .name("test2")
                .age(22)
                .build();
    }
}