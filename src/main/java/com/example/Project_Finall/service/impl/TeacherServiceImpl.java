package com.example.Project_Finall.service.impl;
import com.example.Project_Finall.modell.*;
import com.example.Project_Finall.repository.UsersRepository;
import com.example.Project_Finall.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Random;
@Service
public class TeacherServiceImpl implements TeacherService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TeacherServiceImpl(UsersRepository usersRepository,
                              PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Teacher save(Teacher teacher) {

        if (teacher.getUsername() == null ||
                teacher.getPassword() == null ||
                teacher.getEmail() == null) {
            throw new IllegalArgumentException("Invalid values");
        }
        Teacher teachers=Teacher.builder()
                .username(teacher.getUsername())
                .password(passwordEncoder.encode(teacher.getPassword()))
                .status(Status.WAITING)
                .role(Role.ROLE_TEACHER)
                .email(teacher.getEmail())
                .gender(teacher.getGender())
                .personCode("TCH-" + (100 + new Random().nextInt(900)))
                .build();
        return usersRepository.save(teachers);
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        return usersRepository.findById(id)
                .map(u -> (Teacher) u);
    }

    @Override
    public List<Teacher> findAll() {
        return usersRepository.findAll().stream()
                .filter(u -> u instanceof Teacher)
                .map(u -> (Teacher) u)
                .toList();
    }

    @Override
    public Teacher update(Teacher entity, Long id) {
        Teacher teacher = findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        teacher.setRole(entity.getRole());
        teacher.setPassword(passwordEncoder.encode(entity.getPassword()));
        teacher.setUsername(entity.getUsername());
        teacher.setEmail(entity.getEmail());
        teacher.setGender(entity.getGender());
        return usersRepository.save(teacher);
    }

    @Override
    public void deleteById(Long id) {
        usersRepository.deleteById(id);
    }

    @Override
    public Optional<Teacher> findByUsernameIgnoreCase(String username) {
        Users users = usersRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new RuntimeException("Teacher Not Found"));
        if (users instanceof Teacher){
            Teacher teacher=(Teacher) users;
            return Optional.of(teacher);
        }
        return Optional.empty();
    }
}
