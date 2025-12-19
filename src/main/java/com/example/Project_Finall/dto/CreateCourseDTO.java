package com.example.Project_Finall.dto;


import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateCourseDTO (

        @NotBlank(message = "TitleCourse Is Required")
        String titleCourse,

        @NotNull(message = "StartDate Is Required")
        @FutureOrPresent(message = "StartDate Can Be Today Or In The Future")
        LocalDate startDate,

        @NotNull(message = "EndDate Is Required")
        @Future(message = "EndDate Must Be In The Future")
        LocalDate endDate,

        @NotNull(message = "Capacity Is Required")
        @Min(value = 1, message = "Capacity Must Be At Least 1")
        Integer capacity
){
}
