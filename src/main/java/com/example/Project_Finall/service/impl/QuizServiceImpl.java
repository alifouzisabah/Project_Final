package com.example.Project_Finall.service.impl;

import com.example.Project_Finall.modell.Courses;
import com.example.Project_Finall.modell.Quiz;
import com.example.Project_Finall.modell.Teacher;
import com.example.Project_Finall.repository.CoursesRepository;
import com.example.Project_Finall.repository.QuizRepository;
import com.example.Project_Finall.repository.UsersRepository;
import com.example.Project_Finall.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final UsersRepository usersRepository;
    private final CoursesRepository coursesRepository;

    @Autowired
    public QuizServiceImpl(QuizRepository quizRepository, UsersRepository usersRepository, CoursesRepository coursesRepository) {
        this.quizRepository = quizRepository;
        this.usersRepository = usersRepository;
        this.coursesRepository = coursesRepository;
    }

    @Override
    public Quiz save(Quiz quiz) {
        if (
                quiz.getTitleQuiz() == null ||
                        quiz.getDescription() == null ||
                        quiz.getDurationInMinutes()== 0
        ) {
            throw new IllegalArgumentException("Invalid Value");
        }

        Quiz quizCreate = new Quiz();
        quizCreate.setTitleQuiz(quiz.getTitleQuiz());
//        courses.setCourseCode("" + (10000 + new Random().nextInt(900)));
        quizCreate.setDescription(quiz.getDescription());
        quizCreate.setDurationInMinutes(quiz.getDurationInMinutes());
        quizCreate.setCourses(quiz.getCourses());
        quizCreate.setTeacher(quiz.getTeacher());
        Quiz save = quizRepository.save(quizCreate);
        return save;
    }

    @Override
    public Optional<Quiz> findById(Long aLong) {
        return quizRepository.findById(aLong);
    }

    @Override
    public void deleteById(Long aLong) {
        Quiz quiz = quizRepository.findById(aLong)
                .orElseThrow(() -> new RuntimeException("Quiz Not Found"));

        quiz.getQuestionList().clear();
        quizRepository.delete(quiz);
    }

    @Override
    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    @Override
    public Quiz update(Quiz entity, Long aLong) {
        Quiz quiz = quizRepository.findById(aLong).orElseThrow(() -> new RuntimeException("Quiz Not Found"));

        quiz.setTitleQuiz(entity.getTitleQuiz());
        quiz.setDescription(entity.getDescription());
        quiz.setDurationInMinutes(entity.getDurationInMinutes());
        quiz.setCourses(quiz.getCourses());
        Quiz save = quizRepository.save(quiz);
        return save;
    }

    @Override
    public List<Quiz> findAllByCourses(Courses courses) {
        return quizRepository.findAllByCourses(courses);
    }

    @Override
    public List<Quiz> findAllByTeacher(Teacher teacher) {
        return quizRepository.findAllByTeacher(teacher);
    }
}
