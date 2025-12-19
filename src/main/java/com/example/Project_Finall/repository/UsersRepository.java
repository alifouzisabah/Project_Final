package com.example.Project_Finall.repository;

import com.example.Project_Finall.modell.Gender;
import com.example.Project_Finall.modell.Role;
import com.example.Project_Finall.modell.Status;
import com.example.Project_Finall.modell.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsernameIgnoreCase(String username);

    List<Users> findAllByRole(Role role);

    List<Users> findAllByGender(Gender gender);

    List<Users> findAllByStatus(Status status);

    List<Users> findAllByUsernameIgnoreCase(String username);
}
