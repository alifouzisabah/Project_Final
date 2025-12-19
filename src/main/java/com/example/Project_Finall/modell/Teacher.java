package com.example.Project_Finall.modell;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends Users {

    @OneToMany(mappedBy = "teacher")
    List<Courses> courses=new ArrayList<>();
}
