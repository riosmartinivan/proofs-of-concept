package com.phorus.petservice.model.dtos;

import com.phorus.petservice.model.validationGroups.PetValidationGroup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PetDTOTest {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testDefaultValidations_error() {
        PetDTO petDTO = PetDTO.builder()
                .age(-1)
                .name("ab")
                .build();

        Set<ConstraintViolation<PetDTO>> violations = validator.validate(petDTO,
                PetValidationGroup.PetUpdateValGroup.class);

        assertEquals(2, violations.size());

        violations.forEach(violation -> {
            switch (violation.getPropertyPath().toString()) {
                case "name":
                    assertEquals("Must have at least 3 characters long", violation.getMessage());
                    break;
                case "age":
                    assertEquals("Cannot be negative", violation.getMessage());
                    break;
                default:
                    fail("Unknown violation");
            }
        });
    }

    @Test
    void testDefaultValidations_passed() {
        PetDTO petDTO = new PetDTO();

        Set<ConstraintViolation<PetDTO>> violations = validator.validate(petDTO,
                PetValidationGroup.PetUpdateValGroup.class);

        assertEquals(0, violations.size());
    }

    @Test
    void testCreateValidations_error() {
        PetDTO petDTO = PetDTO.builder()
                .name("")
                .build();

        Set<ConstraintViolation<PetDTO>> violations = validator.validate(petDTO,
                PetValidationGroup.PetCreateValGroup.class);

        assertEquals(4, violations.size());

        violations.forEach(violation -> {
            switch (violation.getPropertyPath().toString()) {
                case "name":
                    assertTrue(violation.getMessage().equals("Cannot be blank") ||
                                    violation.getMessage().equals("Must have at least 3 characters long"),
                            "Unexpected name violation value: " + violation.getMessage());
                    break;
                case "age":
                case "userId":
                    assertEquals("Cannot be null", violation.getMessage());
                    break;
                default:
                    fail("Unknown violation");
            }
        });
    }

    @Test
    void testCreateValidations_passed() {
        PetDTO petDTO = PetDTO.builder()
                .age(1)
                .name("abc")
                .userId(1L)
                .build();

        Set<ConstraintViolation<PetDTO>> violations = validator.validate(petDTO,
                PetValidationGroup.PetCreateValGroup.class);

        assertEquals(0, violations.size());
    }
}