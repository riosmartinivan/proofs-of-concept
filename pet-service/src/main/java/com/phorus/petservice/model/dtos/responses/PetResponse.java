package com.phorus.petservice.model.dtos.responses;

import com.phorus.petservice.model.dtos.PetDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PetResponse extends PetDTO {

    private Long id;
}
