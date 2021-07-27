package com.phorus.userservice.model.dtos;

import com.phorus.userservice.model.validationGroups.UserValidationGroup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testDefaultValidations_error() {
        UserDTO userDTO = UserDTO.builder()
                .age(-1)
                .name("ab")
                .build();

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO,
                UserValidationGroup.UserUpdateValGroup.class);

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
        UserDTO userDTO = new UserDTO();

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO,
                UserValidationGroup.UserUpdateValGroup.class);

        assertEquals(0, violations.size());
    }

    @Test
    void testCreateValidations_error() {
        UserDTO userDTO = UserDTO.builder()
                .name("")
                .build();

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO,
                UserValidationGroup.UserCreateValGroup.class);

        assertEquals(3, violations.size());

        violations.forEach(violation -> {
            switch (violation.getPropertyPath().toString()) {
                case "name":
                    assertTrue(violation.getMessage().equals("Cannot be blank") ||
                            violation.getMessage().equals("Must have at least 3 characters long"),
                            "Unexpected name violation value: " + violation.getMessage());
                    break;
                case "age":
                    assertEquals("Cannot be null", violation.getMessage());
                    break;
                default:
                    fail("Unknown violation");
            }
        });
    }

    @Test
    void testCreateValidations_passed() {
        UserDTO userDTO = UserDTO.builder()
                .age(1)
                .name("abc")
                .build();

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO,
                UserValidationGroup.UserCreateValGroup.class);

        assertEquals(0, violations.size());
    }
}