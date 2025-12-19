package com.example.Project_Finall.modell;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Setter
@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public class BaseModel<ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;
}
