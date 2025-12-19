package com.example.Project_Finall.service.impl;

import com.example.Project_Finall.modell.Role;
import com.example.Project_Finall.modell.Status;
import com.example.Project_Finall.modell.Students;
import com.example.Project_Finall.repository.UsersRepository;
import com.example.Project_Finall.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class StudentsServiceImpl implements StudentsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentsServiceImpl(UsersRepository usersRepository,
                               PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Students save(Students student) {

        if (student.getUsername() == null ||
                student.getPassword() == null ||
                student.getEmail() == null) {
            throw new IllegalArgumentException("Invalid values");
        }
        student.setPersonCode("STD-" + (100 + new Random().nextInt(900)));
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setStatus(Status.WAITING);
        student.setRole(Role.ROLE_STUDENT);

        return usersRepository.save(student);
    }

    @Override
    public Optional<Students> findById(Long id) {
        return usersRepository.findById(id)
                .map(u -> (Students) u);
    }

    @Override
    public List<Students> findAll() {
        return usersRepository.findAll().stream()
                .filter(u -> u instanceof Students)
                .map(u -> (Students) u)
                .toList();
    }

    @Override
    public Students update(Students entity, Long id) {
        Students student = findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setRole(entity.getRole());
        student.setPassword(passwordEncoder.encode(entity.getPassword()));
        student.setUsername(entity.getUsername());
        student.setEmail(entity.getEmail());
        student.setGender(entity.getGender());
        return usersRepository.save(student);
    }

    @Override
    public void deleteById(Long id) {
        usersRepository.deleteById(id);
    }
}