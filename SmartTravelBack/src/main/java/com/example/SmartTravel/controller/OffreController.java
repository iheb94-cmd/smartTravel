
package com.example.SmartTravel.controller;

import com.example.SmartTravel.dto.OffreDto;
import com.example.SmartTravel.service.OffreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent/offres")
public class OffreController {

    private final OffreService offreService;

    public OffreController(OffreService offreService) {
        this.offreService = offreService;
    }

    // 1. Afficher toutes les offres de l'agent connecté
    @GetMapping
    public List<OffreDto> getAll(Authentication authentication) {

        return offreService.getAll(authentication.getName());
    }

    // 2. Afficher une offre par son ID
    @GetMapping("/{id}")
    public OffreDto getById(
            @PathVariable Long id,
            Authentication authentication) {

        return offreService.getById(id, authentication.getName());
    }

    // 3. Ajouter une offre
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OffreDto create(
            @Valid @RequestBody OffreDto dto,
            Authentication authentication) {

        return offreService.create(dto, authentication.getName());
    }

    // 4. Modifier une offre
    @PutMapping("/{id}")
    public OffreDto update(
            @PathVariable Long id,
            @Valid @RequestBody OffreDto dto,
            Authentication authentication) {

        return offreService.update(
                id,
                dto,
                authentication.getName()
        );
    }

    // 5. Supprimer une offre
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            Authentication authentication) {

        offreService.delete(id, authentication.getName());
    }
}
