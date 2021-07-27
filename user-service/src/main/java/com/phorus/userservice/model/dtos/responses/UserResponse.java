package com.phorus.userservice.model.dtos.responses;

import com.phorus.userservice.model.dtos.UserDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends UserDTO {

    private Long id;
}
