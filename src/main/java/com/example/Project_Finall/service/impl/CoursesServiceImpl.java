package com.example.Project_Finall.service.impl;

import com.example.Project_Finall.modell.Courses;
import com.example.Project_Finall.modell.Students;
import com.example.Project_Finall.modell.Teacher;
import com.example.Project_Finall.repository.CoursesRepository;
import com.example.Project_Finall.service.CoursesService;
import com.example.Project_Finall.service.StudentsService;
import com.example.Project_Finall.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CoursesServiceImpl implements CoursesService {
    private final CoursesRepository coursesRepository;
    private final TeacherService teacherService;
    private final StudentsService studentsService;

    @Autowired
    public CoursesServiceImpl(CoursesRepository coursesRepository, TeacherService teacherService, StudentsService studentsService) {
        this.coursesRepository = coursesRepository;
        this.teacherService = teacherService;
        this.studentsService = studentsService;
    }

    @Override
    public Courses save(Courses course) {
        if (
                course.getTitleCourse() == null ||
                        course.getCapacity() == 0 ||
                        course.getStartDate() == null ||
                        course.getEndDate() == null
        ) {
            throw new IllegalArgumentException("Invalid Value");
        }

        Courses courses = new Courses();
        courses.setTitleCourse(course.getTitleCourse());
        courses.setCourseCode("" + (10000 + new Random().nextInt(900)));
        courses.setStartDate(course.getStartDate());
        courses.setEndDate(course.getEndDate());
        courses.setCapacity(course.getCapacity());
        Courses save = coursesRepository.save(courses);
        return save;

    }

    @Override
    public Optional<Courses> findById(Long aLong) {
        return coursesRepository.findById(aLong);
    }

    @Override
    public void deleteById(Long aLong) {
        Courses course = coursesRepository.findById(aLong)
                .orElseThrow(() -> new RuntimeException("Course Not Found"));

        course.getStudents().clear();
        coursesRepository.delete(course);

    }

    @Override
    public List<Courses> findAll() {
        return coursesRepository.findAll();
    }

    @Override
    public Courses update(Courses entity, Long aLong) {
        Courses found = coursesRepository.findById(aLong).orElseThrow(() -> new RuntimeException("Course Not Found"));
        found.setTitleCourse(entity.getTitleCourse());
        found.setStartDate(entity.getStartDate());
        found.setEndDate(entity.getEndDate());
        found.setCapacity(entity.getCapacity());
        return coursesRepository.save(found);
    }

    @Transactional
    @Override
    public boolean addTeacherToCourse(Long courseId, Long teacherID) {
        Courses courses = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course Not Found"));
        Teacher teacher = teacherService.findById(teacherID)
                .orElseThrow(() -> new RuntimeException("Teacher Not Found"));
        if (courses.getTeacher() != null) {
            return false;
        } else {
            courses.setTeacher(teacher);
            teacher.getCourses().add(courses);
            coursesRepository.save(courses);
            return true;
        }
    }

    @Transactional
    @Override
    public ResultAddStudent addStudentToCourse(Long courseId, Long studentId) {
        Courses courses = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course Not Found"));

        Students student = studentsService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student Not Found"));
        if (student.getCourses().contains(courses)) {
            return ResultAddStudent.ADD_BEFORE;
        } else {
            if (courses.getCapacity() == courses.getCountRegister()) {
                return ResultAddStudent.CAPACITY_FULL;
            } else {
                courses.getStudents().add(student);
                courses.setCapacity(courses.getCapacity());
                courses.setCountRegister(courses.getCountRegister() + 1);
                student.getCourses().add(courses);
                coursesRepository.save(courses);
                return ResultAddStudent.SUCCESS;
            }
        }
    }

    @Override
    @Transactional
    public boolean exitTeacherOfCourse(Long courseId, Long teacherID) {
        Courses course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course Not Found"));
        Teacher teacher = teacherService.findById(teacherID)
                .orElseThrow(() -> new RuntimeException("Teacher Not Found"));
        if (course.getTeacher() == null || !course.getTeacher().getId().equals(teacherID)) {
            return false;
        }
        course.setTeacher(null);
        teacher.getCourses().remove(course);
        coursesRepository.save(course);
        return true;
    }

    @Transactional
    @Override
    public boolean exitStudentOfCourse(Long courseId, Long studentId) {
        Courses course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course Not Found"));
        Students student = studentsService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student  Not Found"));

        if (!course.getStudents().contains(student) || !student.getCourses().contains(course)) {
            return false;

        } else {
            course.getStudents().remove(student);
            course.setCapacity(course.getCapacity());
            course.setCountRegister(course.getCountRegister()-1);
            student.getCourses().remove(course);
            coursesRepository.save(course);
            return true;
        }
    }
}
