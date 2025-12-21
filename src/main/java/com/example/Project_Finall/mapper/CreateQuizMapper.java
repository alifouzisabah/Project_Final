package com.example.Project_Finall.mapper;

import com.example.Project_Finall.dto.CreateQuizDTO;
import com.example.Project_Finall.modell.Quiz;
import org.springframework.stereotype.Component;

@Component
public class CreateQuizMapper {
    public Quiz toEntity(CreateQuizDTO dto) {
        Quiz quiz = Quiz.builder()
                .titleQuiz(dto.titleQuiz())
                .description(dto.description())
                .durationInMinutes(dto.timeQuiz())
                .build();
        return quiz;
    }
}
