package com.example.SmartTravel.service;


import com.example.SmartTravel.config.JwtUtil;
import com.example.SmartTravel.dto.LoginResponse;
import com.example.SmartTravel.dto.UserDto;
import com.example.SmartTravel.entity.User;
import com.example.SmartTravel.enums.Role;
import com.example.SmartTravel.mapper.UserMapper;
import com.example.SmartTravel.repository.UserRepository;
import com.example.SmartTravel.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserDto register(RegisterRequest req) {
        if (req.getNom() == null || req.getNom().isBlank()
                || req.getPrenom() == null || req.getPrenom().isBlank()
                || req.getEmail() == null || req.getEmail().isBlank()
                || req.getPassword() == null || req.getPassword().isBlank()) {
            throw new RuntimeException("Veuillez remplir tous les champs obligatoires");
        }
        if (!req.getEmail().matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$")) {
            throw new RuntimeException("Format d'email invalide");
        }
        if (req.getPassword().length() < 6) {
            throw new RuntimeException("Le mot de passe doit contenir au moins 6 caractères");
        }
        String email = req.getEmail().trim().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        User user = new User();
        user.setNom(req.getNom().trim());
        user.setPrenom(req.getPrenom().trim());
        user.setAge(req.getAge());
        user.setPays(req.getPays());
        user.setTelephone(req.getTelephone());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.VOYAGEUR);

        return UserMapper.mapToDto(userRepository.save(user));
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