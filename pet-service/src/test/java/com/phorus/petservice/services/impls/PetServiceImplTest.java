package com.phorus.petservice.services.impls;

import com.phorus.petservice.exceptions.ResourceNotFoundException;
import com.phorus.petservice.model.dbentities.Pet;
import com.phorus.petservice.model.dtos.PetDTO;
import com.phorus.petservice.repositories.PetRepository;
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
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

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
        when(petRepository.findById(eq(1L))).thenReturn(Mono.just(createPet()));

        Mono<Pet> response = petService.findById(1L);

        StepVerifier.create(response)
                .expectNext(createPet())
                .verifyComplete();
    }

    @Test
    void findById_not_found() {
        when(petRepository.findById(eq(1L))).thenReturn(Mono.empty());

        Mono<Pet> response = petService.findById(1L);

        StepVerifier.create(response)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void create() {
        when(petRepository.save(any(Pet.class))).thenReturn(Mono.just(createPet()));

        Mono<Long> response = petService.create(Mono.just(createPet()));

        StepVerifier.create(response)
                .expectNext(1L)
                .verifyComplete();

        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void update() {
        when(petRepository.findById(eq(1L))).thenReturn(Mono.just(createPet()));

        Mono<Void> response = petService.update(1L, Mono.just(createPetDto()));

        StepVerifier.create(response)
                .verifyComplete();

        ArgumentCaptor<Pet> petArgumentCaptor = ArgumentCaptor.forClass(Pet.class);
        verify(petRepository, times(1)).save(petArgumentCaptor.capture());

        assertEquals("pepe2", petArgumentCaptor.getValue().getName());
        assertEquals(6, petArgumentCaptor.getValue().getAge());
    }

    @Test
    void update_only_name() {
        when(petRepository.findById(eq(1L))).thenReturn(Mono.just(createPet()));

        Mono<Void> response = petService.update(1L, Mono.just(PetDTO.builder()
                .name("pepe2").userId(1L).build()));

        StepVerifier.create(response)
                .verifyComplete();

        ArgumentCaptor<Pet> petArgumentCaptor = ArgumentCaptor.forClass(Pet.class);
        verify(petRepository, times(1)).save(petArgumentCaptor.capture());

        assertEquals("pepe2", petArgumentCaptor.getValue().getName());
        assertEquals(5, petArgumentCaptor.getValue().getAge());
    }

    @Test
    void update_not_found() {
        when(petRepository.findById(eq(1L))).thenReturn(Mono.empty());

        Mono<Void> response = petService.update(1L, Mono.just(createPetDto()));

        StepVerifier.create(response)
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(petRepository, times(0)).save(any(Pet.class));
    }

    @Test
    void deleteById() {
        when(petRepository.deleteById(eq(1L))).thenReturn(Mono.empty());

        Mono<Void> response = petService.deleteById(1L);

        StepVerifier.create(response)
                .verifyComplete();

        verify(petRepository, times(1)).deleteById(eq(1L));
    }


    @Test
    void getAmountOfPets() {
        when(petRepository.countByUserId(eq(1L))).thenReturn(Mono.just(5L));

        Mono<Long> response = petService.getAmountOfPets(1L);

        StepVerifier.create(response)
                .expectNext(5L)
                .verifyComplete();
    }


    private Pet createPet() {
        return Pet.builder()
                .id(1L)
                .name("pepe")
                .age(5)
                .userId(1L)
                .build();
    }

    private PetDTO createPetDto() {
        return PetDTO.builder()
                .name("pepe2")
                .age(6)
                .userId(1L)
                .build();
    }
}