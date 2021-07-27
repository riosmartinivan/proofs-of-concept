package com.phorus.petservice.model.dbentities;

import com.phorus.petservice.model.util.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "pets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Pet extends BaseEntity {

    private String name;
    private Integer age;
    private Long userId; // Owner
}
