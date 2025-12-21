package com.example.Project_Finall.controller;

import com.example.Project_Finall.dto.CreateCourseDTO;
import com.example.Project_Finall.dto.RegisterUserDTO;
import com.example.Project_Finall.dto.SearchUserDTO;
import com.example.Project_Finall.mapper.CreateCourseMapper;
import com.example.Project_Finall.mapper.RegisterUser;
import com.example.Project_Finall.modell.*;
import com.example.Project_Finall.service.*;
import com.example.Project_Finall.service.impl.ResultAddStudent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    private final UsersService usersService;
    private final TeacherService teacherService;
    private final StudentsService studentsService;
    private final ManagerService managerService;
    private final RegisterUser registerUser;
    private final CoursesService coursesService;
    private final CreateCourseMapper createCourseMapper;

    @Autowired
    public ManagerController(UsersService usersService, TeacherService teacherService, StudentsService studentsService, ManagerService managerService, RegisterUser registerUser, CoursesService coursesService, CreateCourseMapper createCourseMapper) {
        this.usersService = usersService;
        this.teacherService = teacherService;
        this.studentsService = studentsService;
        this.managerService = managerService;
        this.registerUser = registerUser;
        this.coursesService = coursesService;
        this.createCourseMapper = createCourseMapper;
    }

    @GetMapping("/dashboard")
    public String managerPage(Model model) {
        model.addAttribute("countAccount", usersService.findAll().size());
        model.addAttribute("countCourse", coursesService.findAll().size());
        return "/manager/dashboard";
    }

    //----->USERS
    @GetMapping("/list_account")
    public String listUserPage(Model model) {
        List<Users> users = usersService.findAll();
        model.addAttribute("users", users);
        return "/manager/list_account";
    }

    @PostMapping("/accept_user/{id}")
    public String acceptUser(@PathVariable Long id) {
        Users user = usersService.findById(id)
                .orElseThrow(() -> new RuntimeException("Account Not Found"));
        usersService.acceptStatusUser(user ,id);
        return "redirect:/manager/list_account";
    }

    @GetMapping("/edit_user/{id}")
    public String editUserPage(@PathVariable Long id, Model model) {
        Optional<Users> user = usersService.findById(id);
        if (user.isEmpty()) {
            return "redirect:/manager/list_account";
        } else {
            model.addAttribute("user", user.get());
            return "/manager/edit_user";
        }
    }

    @PostMapping("/edit_user/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute @Valid RegisterUserDTO dto, BindingResult bindingResult, Model model) {
        Optional<Users> foundUser = usersService.findById(id);
        if (foundUser.isEmpty()) {
            model.addAttribute("error", "User Not Found");
            return "/manager/edit_user";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
            model.addAttribute("user", foundUser.get());
            return "/manager/edit_user";
        }

        Users entity = registerUser.toEntityUsers(dto);
        if (!dto.password().equals(dto.repeatPassword())) {
            model.addAttribute("error", "NewPassword With RepeatNewPassword Is Not Matched");
            return "/manager/edit_user";
        }

        Users updatedUser = usersService.update(entity, id);
        model.addAttribute("success", updatedUser.getUsername() + "Updated Successfully");
        model.addAttribute("user", updatedUser);

        return "/manager/edit_user";
    }

    @PostMapping("/delete_user/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes,BindingResult bindingResult) {
        Optional<Users> foundUser = usersService.findById(id);
        if (foundUser.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User Not Found");
            return "redirect:/manager/list_account";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldError());
            return "/manager/edit_course";
        }
        usersService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", foundUser.get().getUsername() + "Deleted Successfully");
        return "redirect:/manager/list_account";
    }


    @GetMapping("/search_users")
    public String searchUsers(@ModelAttribute SearchUserDTO dto, Model model) {
        String username = dto.username() != null && !dto.username().isEmpty() ? dto.username() : null;
        Gender gender = dto.gender() != null && !dto.gender().isEmpty() ? Gender.valueOf(dto.gender()) : null;
        Role role = dto.role() != null && !dto.role().isEmpty() ? Role.valueOf(dto.role()) : null;
        Status status = dto.status() != null && !dto.status().isEmpty() ? Status.valueOf(dto.status()) : null;
        List<Users> users = usersService.search(username,gender,role,status);
        model.addAttribute("users", users);
        return "/manager/list_account";
    }

    //--------->COURSE
    @GetMapping("/list_course")
    public String listCoursePage(Model model) {
        List<Courses> courses = coursesService.findAll();
        model.addAttribute("courses", courses);
        return "/manager/list_course";
    }

    @GetMapping("/create_course")
    public String createCoursePage(Model model) {

        return "/manager/create_course";
    }

    @PostMapping("/create_course")
    public String createCourse(@ModelAttribute @Valid CreateCourseDTO courseDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "/manager/create_course";
        }
        Courses entity = createCourseMapper.toEntity(courseDTO);
        coursesService.save(entity);
        model.addAttribute("success", entity.getTitleCourse() + " Created Successfully");
        return "/manager/create_course";
    }

    @PostMapping("/delete_course/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Courses> foundCourse = coursesService.findById(id);
        if (foundCourse.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Course Not Found");
            return "redirect:/manager/list_course";
        }
        coursesService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", foundCourse.get().getTitleCourse() + "Deleted Successfully");
        return "redirect:/manager/list_course";
    }

    @GetMapping("/edit_course/{id}")
    public String editCoursePage(@PathVariable Long id,
                                 Model model) {
        Optional<Courses> course = coursesService.findById(id);
        if (course.isEmpty()) {
            return "redirect:/manager/list_course";
        } else {
            model.addAttribute("course", course.get());
            return "/manager/edit_course";
        }
    }

    @PostMapping("/edit_course/{id}")
    public String editCourse(@PathVariable Long id, @ModelAttribute @Valid CreateCourseDTO dto, BindingResult bindingResult, Model model) {
        Optional<Courses> foundCourse = coursesService.findById(id);
        if (foundCourse.isEmpty()) {
            model.addAttribute("error", "Course Not Found");
            return "/manager/edit_course";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
            model.addAttribute("course", foundCourse.get());
            return "/manager/edit_course";
        }
        Courses entity = createCourseMapper.toEntity(dto);
        Courses updatedCourse = coursesService.update(entity, id);
        model.addAttribute("success", updatedCourse.getTitleCourse() + " Updated Successfully");
        model.addAttribute("course", updatedCourse);
        return "/manager/edit_course";
    }


    @GetMapping("/detail_course/{id}")
    public String detailCoursePage(@PathVariable Long id, Model model) {
        Optional<Courses> foundCourse = coursesService.findById(id);
        Teacher teacher = foundCourse.get().getTeacher();
        List<Students> students = foundCourse.get().getStudents();
        model.addAttribute("course", foundCourse.get());
        model.addAttribute("teacher", teacher);
        model.addAttribute("students", students);
        return "/manager/detail_course";
    }

    @GetMapping("/select_teacher/{courseId}")
    public String addTeacherPage(@PathVariable Long courseId, Model model) {

        List<Teacher> teachers = teacherService.findAll();

        model.addAttribute("teachers", teachers);
        model.addAttribute("courseId", courseId);

        return "manager/select_teacher";
    }

    @GetMapping("/select_student/{courseId}")
    public String addStudentPage(@PathVariable Long courseId, Model model) {

        List<Students> students = studentsService.findAll();

        model.addAttribute("students", students);
        model.addAttribute("courseId", courseId);

        return "manager/select_student";
    }

    @PostMapping("/add_teacher/{courseId}/{teacherId}")
    public String addTeacher(@PathVariable Long courseId, @PathVariable Long teacherId, RedirectAttributes redirectAttributes) {
        boolean addTeacher = coursesService.addTeacherToCourse(courseId, teacherId);
        if (!addTeacher) {
            redirectAttributes.addFlashAttribute("error", "This Course Already Has a Teacher");
            redirectAttributes.addFlashAttribute("courseId", courseId);
        } else {
            redirectAttributes.addFlashAttribute("success", "Teacher Added To Course");
        }
        return "redirect:/manager/select_teacher/" + courseId;
    }

    @PostMapping("/add_student/{courseId}/{studentId}")
    public String addStudent(@PathVariable Long courseId, @PathVariable Long studentId, RedirectAttributes redirectAttributes
    ) {
        ResultAddStudent addStudent = coursesService.addStudentToCourse(courseId, studentId);

        if (addStudent.equals(ResultAddStudent.CAPACITY_FULL)) {
            redirectAttributes.addFlashAttribute("error", "Capacity This Course Is Full");
            redirectAttributes.addFlashAttribute("courseId", courseId);

        } else if (addStudent.equals(ResultAddStudent.ADD_BEFORE)) {
            redirectAttributes.addFlashAttribute("error", "This Student Add TO Course Before");
            redirectAttributes.addFlashAttribute("courseId", courseId);

        } else {
            redirectAttributes.addFlashAttribute("success", "Student Added To Course");
        }
        return "redirect:/manager/select_student/" + courseId;
    }

    @PostMapping("/exit_teacher/{courseId}/{teacherId}")
    public String exitTeacher(@PathVariable Long courseId, @PathVariable Long teacherId, RedirectAttributes redirectAttributes) {
        boolean teacher = coursesService.exitTeacherOfCourse(courseId, teacherId);
        if (!teacher) {
            redirectAttributes.addFlashAttribute("error", "This Teacher Not Exist OF Course");
        } else {
            redirectAttributes.addFlashAttribute("success", "Teacher Delete Of Course");
        }
        return "redirect:/manager/detail_course/" + courseId;
    }

    @PostMapping("/exit_student/{courseId}/{studentId}")
    public String exitStudent(@PathVariable Long courseId, @PathVariable Long studentId, RedirectAttributes redirectAttributes) {
        boolean removeStudent = coursesService.exitStudentOfCourse(courseId, studentId);
        if (!removeStudent) {
            redirectAttributes.addFlashAttribute("error", "This Student Not Exist OF Course");
            redirectAttributes.addFlashAttribute("courseId", courseId);
        } else {
            redirectAttributes.addFlashAttribute("success", "Student Delete Of Course");
        }
        return "redirect:/manager/detail_course/" + courseId;
    }
}
