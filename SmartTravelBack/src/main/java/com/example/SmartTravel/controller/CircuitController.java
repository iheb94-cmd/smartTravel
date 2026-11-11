
package com.example.SmartTravel.controller;

import com.example.SmartTravel.dto.CircuitDto;
import com.example.SmartTravel.service.CircuitService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent/circuits")
public class CircuitController {

    private final CircuitService circuitService;

    public CircuitController(CircuitService circuitService) {
        this.circuitService = circuitService;
    }

    // GET : afficher tous les circuits ou rechercher
    @GetMapping
    public List<CircuitDto> getAll(
            @RequestParam(required = false) String q) {

        return circuitService.findAll(q);
    }

    // GET : consulter un circuit
    @GetMapping("/{id}")
    public CircuitDto getById(@PathVariable Long id) {

        return circuitService.findById(id);
    }

    // POST : ajouter un circuit
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CircuitDto create(
            @Valid @RequestBody CircuitDto dto,
            Authentication authentication) {

        return circuitService.create(
                dto,
                authentication.getName()
        );
    }

    // PUT : modifier un circuit
    @PutMapping("/{id}")
    public CircuitDto update(
            @PathVariable Long id,
            @Valid @RequestBody CircuitDto dto) {

        return circuitService.update(id, dto);
    }

    // DELETE : supprimer un circuit
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        circuitService.delete(id);
    }
}
