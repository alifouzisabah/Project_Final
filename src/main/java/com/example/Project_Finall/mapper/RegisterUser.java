package com.example.Project_Finall.mapper;

import com.example.Project_Finall.dto.RegisterUserDTO;
import com.example.Project_Finall.modell.*;
import org.springframework.stereotype.Component;

@Component
public class RegisterUser {
    public Users toEntityUsers(RegisterUserDTO dto) {
        Students student = Students.builder()
                .username(dto.username())
                .email(dto.email())
                .gender(Gender.valueOf(dto.gender()))
                .role(Role.valueOf(dto.role()))
                .password(dto.password())
                .build();
        return student;
    }
    public Students toEntityStudent(RegisterUserDTO dto) {
        Students student = Students.builder()
                .username(dto.username())
                .email(dto.email())
                .gender(Gender.valueOf(dto.gender()))
                .role(Role.valueOf(dto.role()))
                .password(dto.password())
                .build();
        return student;
    }
    public Teacher toEntityTeacher(RegisterUserDTO dto) {
        Teacher teacher = Teacher.builder()
                .username(dto.username())
                .email(dto.email())
                .gender(Gender.valueOf(dto.gender()))
                .role(Role.valueOf(dto.role()))
                .password(dto.password())
                .build();
        return teacher;
    }
}
