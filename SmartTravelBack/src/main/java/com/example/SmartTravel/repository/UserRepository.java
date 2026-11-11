package com.example.SmartTravel.repository;


import com.example.SmartTravel.entity.User;
import com.example.SmartTravel.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByRole(Role role);

    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
}