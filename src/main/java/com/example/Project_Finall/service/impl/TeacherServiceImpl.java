package com.example.Project_Finall.service.impl;
import com.example.Project_Finall.modell.Role;
import com.example.Project_Finall.modell.Status;
import com.example.Project_Finall.modell.Teacher;
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

        teacher.setPersonCode("TCH-" + (100 + new Random().nextInt(900)));
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacher.setStatus(Status.WAITING);
        teacher.setRole(Role.ROLE_TEACHER);

        return usersRepository.save(teacher);
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
}
