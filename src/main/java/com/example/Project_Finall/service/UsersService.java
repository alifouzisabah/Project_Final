package com.example.Project_Finall.service;

import com.example.Project_Finall.modell.Gender;
import com.example.Project_Finall.modell.Role;
import com.example.Project_Finall.modell.Status;
import com.example.Project_Finall.modell.Users;

import java.util.List;
import java.util.Optional;

public interface UsersService extends BaseService<Users, Long> {
    Users findByUsername(String username);

    Users updateByUsername(Users user, String username);

    Users resetPassword(String username, String newPassword);

    void acceptStatusUser(Users users,Long userId);

    List<Users> search(String username, Gender gender, Role role, Status status);
}
