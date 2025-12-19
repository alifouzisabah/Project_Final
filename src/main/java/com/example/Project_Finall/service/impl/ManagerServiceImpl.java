package com.example.Project_Finall.service.impl;

import com.example.Project_Finall.modell.Manager;
import com.example.Project_Finall.modell.Role;
import com.example.Project_Finall.modell.Status;
import com.example.Project_Finall.modell.Students;
import com.example.Project_Finall.repository.UsersRepository;
import com.example.Project_Finall.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ManagerServiceImpl implements ManagerService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ManagerServiceImpl(UsersRepository usersRepository,
                               PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Manager save(Manager manager) {
        if (manager.getUsername() == null ||
                manager.getPassword() == null ||
                manager.getEmail() == null) {
            throw new IllegalArgumentException("Invalid values");
        }

        manager.setPersonCode("MNG-" + (100 + new Random().nextInt(900)));
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        manager.setStatus(Status.ACCEPTED);
        manager.setRole(Role.ROLE_MANAGER);

        return usersRepository.save(manager);
    }

    @Override
    public Optional<Manager> findById(Long aLong) {
         return usersRepository.findById(aLong)
                .map(u -> (Manager) u);
    }

    @Override
    public void deleteById(Long aLong) {
        usersRepository.deleteById(aLong);
    }

    @Override
    public List<Manager> findAll() {
        return usersRepository.findAll().stream()
                .filter(u -> u instanceof Manager)
                .map(u -> (Manager) u)
                .toList();    }

    @Override
    public Manager update(Manager entity, Long aLong) {
        Manager manager = findById(aLong)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        manager.setRole(entity.getRole());
        manager.setPassword(passwordEncoder.encode(entity.getPassword()));
        manager.setUsername(entity.getUsername());
        manager.setEmail(entity.getEmail());
        manager.setGender(entity.getGender());
        return usersRepository.save(manager);    }
}
