package com.example.Project_Finall.modell;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "quiz")
public class Quiz extends BaseModel<Long>{

    private String titleQuiz;

    private String description;

    private Integer durationInMinutes;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courses_id", nullable = false)
    private Courses courses;
    @OneToMany(mappedBy = "quiz")
    private List<Question> questionList = new ArrayList<>();






}
