package com.example.Project_Finall.service;

import com.example.Project_Finall.modell.Teacher;
import com.example.Project_Finall.modell.Users;

import java.util.Optional;

public interface TeacherService extends BaseService<Teacher,Long> {
    Optional<Teacher> findByUsernameIgnoreCase(String username);

}
