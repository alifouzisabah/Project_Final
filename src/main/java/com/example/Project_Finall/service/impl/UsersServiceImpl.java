package com.example.Project_Finall.service.impl;

import com.example.Project_Finall.modell.*;
import com.example.Project_Finall.repository.UsersRepository;
import com.example.Project_Finall.service.UsersService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.Project_Finall.modell.Role.*;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users save(Users user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new RuntimeException("Invalid Value");
        }
        if (usersRepository.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        //***
        Users newUser;
        switch (user.getRole()) {
            case ROLE_STUDENT:
                newUser = new Students();
                newUser.setRole(Role.ROLE_STUDENT);
                newUser.setStatus(Status.WAITING);
                newUser.setPersonCode("STD-" + (100 + new Random().nextInt(900)));
                break;
            case ROLE_TEACHER:
                newUser = new Teacher();
                newUser.setRole(ROLE_TEACHER);
                newUser.setStatus(Status.WAITING);
                newUser.setPersonCode("TCH-" + (100 + new Random().nextInt(900)));
                break;
            case ROLE_MANAGER:
                newUser = new Manager();
                newUser.setRole(ROLE_MANAGER);
                newUser.setStatus(Status.ACCEPTED);
                newUser.setPersonCode("MNG-" + (100 + new Random().nextInt(900)));
                break;
            default:
                throw new IllegalArgumentException("Role not supported");
        }
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setGender(user.getGender());
        return usersRepository.save(newUser);
    }

    @Override
    public Optional<Users> findById(Long aLong) {
        return usersRepository.findById(aLong);
    }

    @Override
    public void deleteById(Long aLong) {
        Users users = usersRepository.findById(aLong)
                .orElseThrow(() -> new RuntimeException("Users Not Found"));
        if (users instanceof Students) {
            Students student = (Students) users;

            if (!student.getCourses().isEmpty()) {
                throw new RuntimeException("First delete courses");
            }

            usersRepository.delete(student);
        } else if (users instanceof Teacher) {
            Teacher teacher = (Teacher) users;
            if (!teacher.getCourses().isEmpty()) {
                throw new RuntimeException("First Delete Course");
            }
            usersRepository.delete(teacher);
        } else if (users instanceof Manager) {
            Manager manager = (Manager) users;
            usersRepository.delete(manager);
        }
    }

    @Override
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    //
    @Transactional
    @Override
    public Users update(Users entity, Long userId) {
        Users oldUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (oldUser instanceof Students) {
            ((Students) oldUser).getCourses().forEach(course -> course.getStudents().remove(oldUser));
            ((Students) oldUser).getCourses().clear();
        } else if (oldUser instanceof Teacher) {
            ((Teacher) oldUser).getCourses().forEach(course -> course.setTeacher(null));
            ((Teacher) oldUser).getCourses().clear();
        }
        usersRepository.delete(oldUser);
        usersRepository.flush();
        Users newUser;
        switch (entity.getRole()) {
            case ROLE_STUDENT:
                newUser = Students.builder()
                        .username(entity.getUsername())
                        .password(passwordEncoder.encode(entity.getPassword()))
                        .email(entity.getEmail())
                        .personCode("STD-" + (100 + new Random().nextInt(900)))
                        .gender(entity.getGender())
                        .role(ROLE_STUDENT)
                        .status(oldUser.getStatus())
                        .build();
                break;
            case ROLE_TEACHER:
                newUser = Teacher.builder()
                        .username(entity.getUsername())
                        .password(passwordEncoder.encode(entity.getPassword()))
                        .email(entity.getEmail())
                        .personCode("TCH-" + (100 + new Random().nextInt(900)))
                        .gender(entity.getGender())
                        .role(ROLE_TEACHER)
                        .status(oldUser.getStatus())
                        .build();
                break;
            case ROLE_MANAGER:
                newUser = Manager.builder()
                        .username(entity.getUsername())
                        .password(passwordEncoder.encode(entity.getPassword()))
                        .email(entity.getEmail())
                        .personCode("MNG-" + (100 + new Random().nextInt(900)))
                        .gender(entity.getGender())
                        .role(ROLE_MANAGER)
                        .status(oldUser.getStatus())
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Invalid role");
        }

        usersRepository.save(newUser);
        return newUser;
    }


    @Override
    public Users findByUsername(String username) {
        Optional<Users> user = usersRepository.findByUsernameIgnoreCase(username);
        return user.orElse(null);
    }

    @Override
    public Users updateByUsername(Users user, String username) {
        Users foundUsername = usersRepository.findByUsernameIgnoreCase(username).get();
        if (foundUsername != null) {
            foundUsername.setPassword(user.getPassword());
            Users save = usersRepository.save(foundUsername);
            System.out.println("Account " + username + "Updated");
            return save;
        } else throw new RuntimeException("Account Not Found");
    }

    @Override
    public Users resetPassword(String username, String newPassword) {
        Optional<Users> user = usersRepository.findByUsernameIgnoreCase(username);
        if (user.isEmpty()) return null;

        user.get().setPassword(passwordEncoder.encode(newPassword));
        updateByUsername(user.get(), username);

        System.out.println("Password Account " + username + "Changed");
        return user.get();
    }

    @Override
    public void acceptStatusUser(Users users,Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        if (user.getStatus() != Status.ACCEPTED) {
            user.setStatus(Status.ACCEPTED);
            usersRepository.save(user);
            System.out.println("Status Account Is Accept");
        } else {
            System.out.println("Account Already Accepted");
        }
    }

    //specification
    //criteria bulider
    @Override
    public List<Users> search(String username, Gender gender, Role role, Status status) {
        List<Users> result = usersRepository.findAll();
        if (username != null && !username.isEmpty()) {
            result.retainAll(usersRepository.findAllByUsernameIgnoreCaseStartingWith(username));
        }
        if (gender != null) {
            result.retainAll(usersRepository.findAllByGender(gender));
        }
        if (role != null) {
            result.retainAll(usersRepository.findAllByRole(role));
        }
        if (status != null) {
            result.retainAll(usersRepository.findAllByStatus(status));
        }
        List<Users> filteredUsers = result.stream()
                .filter(u -> u instanceof Students ||
                        u instanceof Teacher ||
                        u instanceof Manager)
                .toList();
        return filteredUsers;
    }
}
