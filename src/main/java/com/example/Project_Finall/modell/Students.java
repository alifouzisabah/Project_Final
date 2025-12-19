package com.example.Project_Finall.modell;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
@DiscriminatorValue("STUDENT")

public class Students extends Users{

    @ManyToMany(mappedBy = "students")
    private List<Courses> courses=new ArrayList<>();


}
