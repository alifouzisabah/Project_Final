package com.example.Project_Finall.modell;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "question")
public abstract class Question extends BaseModel<Long>{
    private String questionText;
    @ManyToOne
    private Teacher teacher;
    @ManyToOne
    private Quiz quiz;

}
