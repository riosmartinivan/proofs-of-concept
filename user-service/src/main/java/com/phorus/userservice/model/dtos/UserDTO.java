package com.phorus.userservice.model.dtos;

import com.phorus.userservice.model.validationGroups.UserValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserDTO {

    @NotBlank(groups = UserValidationGroup.UserCreateValGroup.class, message = "Cannot be blank")
    @Size(groups = {UserValidationGroup.UserCreateValGroup.class, UserValidationGroup.UserUpdateValGroup.class},
            min = 3,
            message = "Must have at least 3 characters long")
    private String name;

    @NotNull(groups = UserValidationGroup.UserCreateValGroup.class, message = "Cannot be null")
    @Min(groups = {UserValidationGroup.UserCreateValGroup.class, UserValidationGroup.UserUpdateValGroup.class},
            value = 0,
            message = "Cannot be negative")
    private Integer age;
}