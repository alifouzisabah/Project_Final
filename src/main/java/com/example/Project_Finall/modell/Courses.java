package com.example.Project_Finall.modell;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "course")
public class Courses extends BaseModel<Long>{
    @Column(nullable = false,unique = true)
    private String titleCourse;
    @Column(nullable = false,unique = true)
    private String courseCode;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private Integer capacity;
    private Integer countRegister=0;
    @ManyToMany
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Students> students = new ArrayList<>();
    @OneToMany(mappedBy = "courses")
    private List<Quiz> quizList=new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}
