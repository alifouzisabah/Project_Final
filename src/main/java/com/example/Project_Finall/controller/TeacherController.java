package com.example.Project_Finall.controller;

import com.example.Project_Finall.dto.CreateQuizDTO;
import com.example.Project_Finall.mapper.CreateQuizMapper;
import com.example.Project_Finall.modell.Courses;
import com.example.Project_Finall.modell.Quiz;
import com.example.Project_Finall.modell.Teacher;
import com.example.Project_Finall.service.CoursesService;
import com.example.Project_Finall.service.QuizService;
import com.example.Project_Finall.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    private TeacherService teacherService;
    private QuizService quizService;
    private CoursesService coursesService;
    private final CreateQuizMapper createQuizMapper;

    @Autowired
    public TeacherController(CreateQuizMapper createQuizMapper, CoursesService coursesService, QuizService quizService, TeacherService teacherService) {
        this.createQuizMapper = createQuizMapper;
        this.coursesService = coursesService;
        this.quizService = quizService;
        this.teacherService = teacherService;
    }

    @GetMapping("/dashboard")
    public String dashboardTeacher(Authentication authentication, Model model) {
        String name = authentication.getName();
        Optional<Teacher> teacher = teacherService.findByUsernameIgnoreCase(name);
        List<Courses> coursesList = teacher.get().getCourses();
        model.addAttribute("teacher", teacher.get());
        model.addAttribute("lisCourses", coursesList);
        return "/teacher/dashboard";

    }

    @GetMapping("/list_quiz/{courseId}")
    public String listQuizPage(@PathVariable Long courseId, Authentication authentication, Model model) {
        String username = authentication.getName();
        Optional<Teacher> teacher = teacherService.findByUsernameIgnoreCase(username);
        model.addAttribute("teacher", teacher.get());
        Optional<Courses> course = coursesService.findById(courseId);
        List<Quiz> alLQuizByCourse = quizService.findAllByCourses(course.get());
        List<Quiz> allQuizByTeacher=quizService.findAllByTeacher(teacher.get());
        model.addAttribute("listQuizByCourse", alLQuizByCourse);
        model.addAttribute("listQuizByTeacher", allQuizByTeacher);

        model.addAttribute("course", course.get());
        return "/teacher/list_quiz";
    }

    @GetMapping("/create_quiz/{courseId}")
    public String addQuizPage(
            @PathVariable Long courseId,
            Authentication authentication,
            Model model
    ) {
        String username = authentication.getName();
        Optional<Teacher> teacher = teacherService.findByUsernameIgnoreCase(username);
        model.addAttribute("teacher", teacher.get());
        Optional<Courses> course = coursesService.findById(courseId);
        model.addAttribute("course", course.get());
        return "/teacher/create_quiz";
    }

    @PostMapping("/create_quiz/{courseId}")
    public String addQuiz(
            @PathVariable Long courseId,
            @ModelAttribute CreateQuizDTO dto,
            Authentication authentication
    ) {
        Teacher teacher = teacherService
                .findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Courses course = coursesService
                .findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Quiz quiz = createQuizMapper.toEntity(dto);
        quiz.setTeacher(teacher);
        quiz.setCourses(course);
        course.getQuizList().add(quiz);

        quizService.save(quiz);

        return "redirect:/teacher/list_quiz/" + courseId;
    }
    @GetMapping("/edit_quiz/{quizId}")
    public String editQuizPage(@PathVariable Long quizId, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        Teacher teacher = teacherService.findByUsernameIgnoreCase(authentication.getName()).orElseThrow();
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        if (!quiz.getTeacher().getId().equals(teacher.getId())) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/teacher/dashboard";
        }
        if (quiz.getCourses() == null) {
            redirectAttributes.addFlashAttribute("error", "Quiz is not linked to any Course!");
            return "redirect:/teacher/dashboard";
        }

        model.addAttribute("teacher", teacher);
        model.addAttribute("quiz", quiz);
        model.addAttribute("course", quiz.getCourses());

        return "/teacher/edit_quiz";
    }



    @PostMapping("/edit_quiz/{quizId}")
    public String editQuiz(@PathVariable Long quizId, @ModelAttribute CreateQuizDTO dto, RedirectAttributes redirectAttributes, Authentication authentication) {
        Teacher teacher = teacherService.findByUsernameIgnoreCase(authentication.getName()).orElseThrow();
        Quiz quiz = quizService.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        if (!quiz.getTeacher().getId().equals(teacher.getId())) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/teacher/dashboard";
        }
        Quiz quiz1 = createQuizMapper.toEntity(dto);
        quizService.update(quiz1,quizId);
        redirectAttributes.addFlashAttribute("success", quiz.getTitleQuiz() + " updated successfully");
        return "redirect:/teacher/list_quiz/" + quiz.getCourses().getId();
    }
    @PostMapping("/delete_quiz/{quizId}")
    public String deleteQuiz(@PathVariable Long quizId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        Teacher teacher = teacherService
                .findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        if (!quiz.getTeacher().getId().equals(teacher.getId())) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/teacher/list_quiz/" + (quiz.getCourses() != null ? quiz.getCourses().getId() : "");
        }
        quizService.deleteById(quizId);

        redirectAttributes.addFlashAttribute("success", quiz.getTitleQuiz() + " by " + teacher.getUsername() + " deleted successfully");

        Long courseId = quiz.getCourses() != null ? quiz.getCourses().getId() : null;
        if (courseId != null) {
            return "redirect:/teacher/list_quiz/" +quiz.getCourses().getId();
        } else {
            return "redirect:/teacher/dashboard";
        }
    }




}
