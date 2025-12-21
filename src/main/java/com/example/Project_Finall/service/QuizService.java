package com.example.Project_Finall.service;

import com.example.Project_Finall.modell.Courses;
import com.example.Project_Finall.modell.Quiz;
import com.example.Project_Finall.modell.Teacher;

import java.util.List;

public interface QuizService extends BaseService<Quiz,Long> {
    List<Quiz> findAllByCourses(Courses courses);
    List<Quiz> findAllByTeacher(Teacher teacher);

}
