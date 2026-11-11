package com.example.SmartTravel.controller;

import com.example.SmartTravel.dto.VolDto;
import com.example.SmartTravel.service.VolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent/vols")
@RequiredArgsConstructor
public class VolController {

    private final VolService volService;

    @GetMapping
    public List<VolDto> list(@RequestParam(required = false) String q) {
        return volService.findAll(q);
    }

    @GetMapping("/{id}")
    public VolDto get(@PathVariable Long id) {
        return volService.findById(id);
    }

    @PostMapping
    public ResponseEntity<VolDto> create(@Valid @RequestBody VolDto dto, Authentication authentication) {
        // authentication.getName() = email de l'agent, mis par le JwtFilter
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(volService.create(dto, authentication.getName()));
    }

    @PutMapping("/{id}")
    public VolDto update(@PathVariable Long id, @Valid @RequestBody VolDto dto) {
        return volService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        volService.delete(id);
        return ResponseEntity.noContent().build();
    }
}