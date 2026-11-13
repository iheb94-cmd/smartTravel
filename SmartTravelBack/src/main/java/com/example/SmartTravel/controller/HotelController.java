
package com.example.SmartTravel.controller;

import com.example.SmartTravel.dto.HotelDto;
import com.example.SmartTravel.service.HotelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    // GET : afficher la liste des hôtels
    @GetMapping
    public List<HotelDto> list(
            @RequestParam(required = false) String q) {

        return hotelService.findAll(q);
    }

    // GET : afficher un hôtel
    @GetMapping("/{id}")
    public HotelDto get(@PathVariable Long id) {
        return hotelService.findById(id);
    }

    // POST : ajouter un hôtel
    @PostMapping
    public ResponseEntity<HotelDto> create(
            @Valid @RequestBody HotelDto dto,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelService.create(
                        dto,
                        authentication.getName()
                ));
    }

    // PUT : modifier un hôtel
    @PutMapping("/{id}")
    public HotelDto update(
            @PathVariable Long id,
            @Valid @RequestBody HotelDto dto) {

        return hotelService.update(id, dto);
    }

    // DELETE : supprimer un hôtel
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        hotelService.delete(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/public/{id}")
    public HotelDto getByIdPublic(@PathVariable Long id) {
        return hotelService.getByIdPublic(id);
    }
}
