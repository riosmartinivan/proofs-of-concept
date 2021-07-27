package com.phorus.petservice.model.dtos;

import com.phorus.petservice.model.validationGroups.PetValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PetDTO {

    @NotBlank(groups = PetValidationGroup.PetCreateValGroup.class, message = "Cannot be blank")
    @Size(groups = {PetValidationGroup.PetCreateValGroup.class,
            PetValidationGroup.PetUpdateValGroup.class},
            min = 3,
            message = "Must have at least 3 characters long")
    private String name;

    @NotNull(groups = PetValidationGroup.PetCreateValGroup.class, message = "Cannot be null")
    @Min(groups = {PetValidationGroup.PetCreateValGroup.class,
            PetValidationGroup.PetUpdateValGroup.class},
            value = 0,
            message = "Cannot be negative")
    private Integer age;

    @NotNull(groups = PetValidationGroup.PetCreateValGroup.class, message = "Cannot be null")
    private Long userId; // Owner
}