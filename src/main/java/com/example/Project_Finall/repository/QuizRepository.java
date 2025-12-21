package com.example.Project_Finall.repository;

import com.example.Project_Finall.modell.Courses;
import com.example.Project_Finall.modell.Quiz;
import com.example.Project_Finall.modell.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
    List<Quiz> findAllByCourses(Courses courses);

    List<Quiz> findAllByTeacher(Teacher teacher);
}
