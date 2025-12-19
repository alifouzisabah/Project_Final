package com.example.Project_Finall.mapper;

import com.example.Project_Finall.dto.CreateCourseDTO;
import com.example.Project_Finall.dto.RegisterUserDTO;
import com.example.Project_Finall.modell.Courses;
import com.example.Project_Finall.modell.Gender;
import com.example.Project_Finall.modell.Role;
import com.example.Project_Finall.modell.Students;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CreateCourseMapper {
    public Courses toEntity(CreateCourseDTO dto) {
        Courses course = Courses.builder()
                .titleCourse(dto.titleCourse())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .capacity(dto.capacity())
                .build();
        return course;
    }
}
