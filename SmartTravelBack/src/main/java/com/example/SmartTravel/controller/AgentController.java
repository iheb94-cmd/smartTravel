
package com.example.SmartTravel.controller;

import com.example.SmartTravel.dto.CreateUserDto;
import com.example.SmartTravel.dto.UserDto;
import com.example.SmartTravel.service.AgentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/agents")
//@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    // 1. Ajouter un nouvel agent
    // POST /api/admin/agents
    @PostMapping
    public ResponseEntity<UserDto> createAgent(
            @RequestBody CreateUserDto request) {

        UserDto createdAgent = agentService.createAgent(request);

        return new ResponseEntity<>(
                createdAgent,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllAgents() {

        return ResponseEntity.ok(
                agentService.getAllAgents()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getAgentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                agentService.getAgentById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateAgent(
            @PathVariable Long id,
            @RequestBody CreateUserDto request) {

        return ResponseEntity.ok(
                agentService.updateAgent(id, request)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAgent(
            @PathVariable Long id) {

        agentService.deleteAgent(id);

        return ResponseEntity.ok(
                "Agent supprimé avec succès"
        );
    }
}
