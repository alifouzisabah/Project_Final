package com.example.Project_Finall.service;

import com.example.Project_Finall.modell.Courses;
import com.example.Project_Finall.service.impl.ResultAddStudent;

public interface CoursesService extends BaseService<Courses, Long> {

    boolean addTeacherToCourse(Long courseId, Long teacherID);

    ResultAddStudent addStudentToCourse(Long courseId, Long studentId);

    boolean exitTeacherOfCourse(Long courseId, Long teacherID);

    boolean exitStudentOfCourse(Long courseId, Long studentId);
}
