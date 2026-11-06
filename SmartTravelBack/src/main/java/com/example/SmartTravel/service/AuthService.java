package com.example.SmartTravel.service;


import com.example.SmartTravel.config.JwtUtil;
import com.example.SmartTravel.dto.LoginResponse;
import com.example.SmartTravel.dto.UserDto;
import com.example.SmartTravel.entity.User;
import com.example.SmartTravel.enums.Role;
import com.example.SmartTravel.mapper.UserMapper;
import com.example.SmartTravel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.VOYAGEUR);
        return userRepository.save(user);
    }

    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        UserDto userDto = UserMapper.mapToDto(user);

        return new LoginResponse(token, userDto);
    }
}