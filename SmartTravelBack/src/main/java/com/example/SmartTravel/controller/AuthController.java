        package com.example.SmartTravel.controller;

import com.example.SmartTravel.dto.LoginRequest;
import com.example.SmartTravel.dto.LoginResponse;
import com.example.SmartTravel.dto.UserDto;
import com.example.SmartTravel.entity.User;
import com.example.SmartTravel.enums.Role;
import com.example.SmartTravel.mapper.UserMapper;
import com.example.SmartTravel.repository.UserRepository;
import com.example.SmartTravel.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register-first-admin")
    public ResponseEntity<UserDto> registerFirstAdmin(
            @RequestBody User user) {

        boolean hasAdmin = userRepository.existsByRole(Role.ADMIN);

        if (hasAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);

        User saved = userRepository.save(user);

        return ResponseEntity.ok(UserMapper.mapToDto(saved));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(response);
    }
}
