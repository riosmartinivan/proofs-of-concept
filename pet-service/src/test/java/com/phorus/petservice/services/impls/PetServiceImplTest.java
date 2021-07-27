package com.phorus.petservice.services.impls;

import com.phorus.petservice.exceptions.ResourceNotFoundException;
import com.phorus.petservice.model.dbentities.Pet;
import com.phorus.petservice.model.dtos.PetDTO;
import com.phorus.petservice.repositories.PetRepository;
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
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @Test
    void findById() {
        when(petRepository.findById(eq(1L))).thenReturn(Optional.of(createPet()));

        Pet response = petService.findById(1L);

        assertEquals(response, createPet());
    }

    @Test
    void findById_not_found() {
        when(petRepository.findById(eq(1L))).thenReturn(Optional.empty());

        try {
            petService.findById(1L);
        } catch (ResourceNotFoundException ex) {
            assertEquals("Pet not found with id: 1", ex.getMessage());
        }
    }

    @Test
    void create() {
        when(petRepository.save(any(Pet.class))).thenReturn(createPet());

        Long response = petService.create(createPet());

        assertEquals(1L, response);

        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void update() {
        when(petRepository.findById(eq(1L))).thenReturn(Optional.of(createPet()));

        petService.update(1L, createPetDto());

        ArgumentCaptor<Pet> petArgumentCaptor = ArgumentCaptor.forClass(Pet.class);
        verify(petRepository, times(1)).save(petArgumentCaptor.capture());

        assertEquals("pepe2", petArgumentCaptor.getValue().getName());
        assertEquals(6, petArgumentCaptor.getValue().getAge());
    }

    @Test
    void update_only_name() {
        when(petRepository.findById(eq(1L))).thenReturn(Optional.of(createPet()));

        petService.update(1L, PetDTO.builder().name("pepe2").build());

        ArgumentCaptor<Pet> petArgumentCaptor = ArgumentCaptor.forClass(Pet.class);
        verify(petRepository, times(1)).save(petArgumentCaptor.capture());

        assertEquals("pepe2", petArgumentCaptor.getValue().getName());
        assertEquals(5, petArgumentCaptor.getValue().getAge());
    }

    @Test
    void update_not_found() {
        when(petRepository.findById(eq(1L))).thenReturn(Optional.empty());

        try {
            petService.update(1L, createPetDto());
            fail("Exception not thrown");
        } catch (ResourceNotFoundException ex) {
            assertEquals("Pet not found with id: 1", ex.getMessage());
        }

        verify(petRepository, times(0)).save(any(Pet.class));
    }

    @Test
    void deleteById() {
        petService.deleteById(1L);

        verify(petRepository, times(1)).deleteById(eq(1L));
    }


    @Test
    void getAmountOfPets() {
        when(petRepository.countByUserId(1L)).thenReturn(5L);

        Long response = petService.getAmountOfPets(1L);

        assertEquals(5L, response);
    }


    private Pet createPet() {
        return Pet.builder()
                .id(1L)
                .name("pepe")
                .age(5)
                .build();
    }

    private PetDTO createPetDto() {
        return PetDTO.builder()
                .name("pepe2")
                .age(6)
                .build();
    }
}