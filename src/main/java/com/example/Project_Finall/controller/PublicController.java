package com.example.Project_Finall.controller;

import com.example.Project_Finall.dto.RegisterUserDTO;
import com.example.Project_Finall.dto.ResetPasswordDTO;
import com.example.Project_Finall.mapper.RegisterUser;
import com.example.Project_Finall.modell.Students;
import com.example.Project_Finall.modell.Teacher;
import com.example.Project_Finall.modell.Users;
import com.example.Project_Finall.service.StudentsService;
import com.example.Project_Finall.service.TeacherService;
import com.example.Project_Finall.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PublicController {
    private final UsersService usersService;
    private final TeacherService teacherService;
    private final StudentsService studentsService;
    private final RegisterUser registerUserMapper;

    @Autowired
    public PublicController(UsersService usersService, TeacherService teacherService, StudentsService studentsService, RegisterUser registerUserMapper) {
        this.usersService = usersService;
        this.teacherService = teacherService;
        this.studentsService = studentsService;
        this.registerUserMapper = registerUserMapper;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid RegisterUserDTO registerDTO,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "/register";
        }
        if (registerDTO.role().equals("ROLE_STUDENT")) {
            Students student = registerUserMapper.toEntityStudent(registerDTO);
            Users user = usersService.findByUsername(student.getUsername());
            if (user!=null){
                model.addAttribute("error",  user.getUsername()+" IS Exist");
                return "register";
            }
            studentsService.save(student);
            model.addAttribute("success", " Student Successfully ");
            return "login";
        } else {
            Teacher teacher = registerUserMapper.toEntityTeacher(registerDTO);
            Users user = usersService.findByUsername(teacher.getUsername());
            if (user!=null){
                model.addAttribute("error",  user.getUsername()+" IS Exist");
                return "register";
            }
            teacherService.save(teacher);
            model.addAttribute("success", "Teacher Successfully");
            return "login";
        }

    }

    @GetMapping("/forget_password")
    public String forgotPasswordPage() {
        return "forget_password";
    }


    @PostMapping("/forget_password")
    public String forgetPassword(
            @ModelAttribute @Valid ResetPasswordDTO dto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "/forget_password";
        }
        if (!dto.newPassword().equals(dto.repeatNewPassword())) {
            model.addAttribute("error", "New Password With RepeatNewPassword Not Matched");
            return "forget_password";
        }

        Users result = usersService.resetPassword(
                dto.username(),
                dto.newPassword()
        );

        if (result==null) {
            model.addAttribute("error", "User Not Found");
            return "forget_password";
        }

        model.addAttribute("success", "Password Changed Successfully");
        return "redirect:/login";
    }


}
