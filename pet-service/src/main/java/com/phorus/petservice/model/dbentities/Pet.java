package com.phorus.petservice.model.dbentities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("pets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode
public class Pet {

    @Id
    private Long id;

    private String name;
    private Integer age;
    private Long userId; // Owner
}
