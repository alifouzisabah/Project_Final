package com.example.Project_Finall.config;


import com.example.Project_Finall.modell.*;
import com.example.Project_Finall.service.ManagerService;
import com.example.Project_Finall.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializerService implements CommandLineRunner {
    private final ManagerService managerService;

    @Autowired
    public DataInitializerService(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Override
    public void run(String... args) {

        List<Manager> managers = managerService.findAll();
        if (!managers.isEmpty()) {
            System.out.println("Manager already exists");
            return;
        }
        Manager manager = new Manager();
        manager.setStatus(Status.ACCEPTED);
        manager.setEmail("manager@gmail.com");
        manager.setRole(Role.ROLE_MANAGER);
        manager.setUsername("manager");
        manager.setGender(Gender.MALE);
        manager.setPassword("manager");
        managerService.save(manager);
        System.out.println("Manager added successfully");
    }
}
