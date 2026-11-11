
package com.example.SmartTravel.service;

import com.example.SmartTravel.dto.CreateUserDto;
import com.example.SmartTravel.dto.UserDto;
import com.example.SmartTravel.entity.User;
import com.example.SmartTravel.enums.Role;
import com.example.SmartTravel.mapper.UserMapper;
import com.example.SmartTravel.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. Ajouter un nouvel agent
    public UserDto createAgent(CreateUserDto request) {

        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé !");
        }
        // Convertir DTO en Entity
        User agent = UserMapper.mapCreateUserDtoToEntity(request);

        // Encoder le mot de passe
        agent.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Attribuer automatiquement le rôle AGENT_AGENCE
        agent.setRole(Role.AGENT_AGENCE);

        // Enregistrer dans la base de données
        User savedAgent = userRepository.save(agent);

        // Retourner un DTO
        return UserMapper.mapToDto(savedAgent);
    }

    // 2. Récupérer tous les agents
    public List<UserDto> getAllAgents() {

        return userRepository.findByRole(Role.AGENT_AGENCE)
                .stream()
                .map(UserMapper::mapToDto)
                .toList();
    }

    // 3. Récupérer un agent par son ID
    public UserDto getAgentById(Long id) {

        User agent = userRepository.findById(id)
                .filter(user -> user.getRole() == Role.AGENT_AGENCE)
                .orElseThrow(() ->
                        new RuntimeException("Agent introuvable !")
                );

        return UserMapper.mapToDto(agent);
    }

    // 4. Modifier un agent
    public UserDto updateAgent(Long id, CreateUserDto updatedRequest) {

        User existingAgent = userRepository.findById(id)
                .filter(user -> user.getRole() == Role.AGENT_AGENCE)
                .orElseThrow(() ->
                        new RuntimeException("Agent introuvable !")
                );

        // Vérifier si le nouvel email est déjà utilisé
        if (updatedRequest.getEmail() != null
                && !updatedRequest.getEmail().equals(existingAgent.getEmail())
                && userRepository.findByEmail(updatedRequest.getEmail()).isPresent()) {

            throw new RuntimeException("Cet email est déjà utilisé !");
        }

        // Modifier les informations personnelles
        existingAgent.setNom(updatedRequest.getNom());
        existingAgent.setPrenom(updatedRequest.getPrenom());
        existingAgent.setEmail(updatedRequest.getEmail());
        existingAgent.setTelephone(updatedRequest.getTelephone());

        // Modifier le mot de passe uniquement s'il est fourni
        if (updatedRequest.getPassword() != null
                && !updatedRequest.getPassword().isEmpty()) {

            existingAgent.setPassword(
                    passwordEncoder.encode(updatedRequest.getPassword())
            );
        }

        // Enregistrer les modifications
        User savedAgent = userRepository.save(existingAgent);

        return UserMapper.mapToDto(savedAgent);
    }

    // 5. Supprimer un agent
    public void deleteAgent(Long id) {

        User agent = userRepository.findById(id)
                .filter(user -> user.getRole() == Role.AGENT_AGENCE)
                .orElseThrow(() ->
                        new RuntimeException("Agent introuvable !")
                );
        userRepository.delete(agent);
    }
}
