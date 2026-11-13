
package com.example.SmartTravel.service;

import com.example.SmartTravel.dto.CircuitDto;
import com.example.SmartTravel.dto.CircuitMapper;
import com.example.SmartTravel.entity.Circuit;
import com.example.SmartTravel.entity.User;
import com.example.SmartTravel.repository.CircuitRepository;
import com.example.SmartTravel.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CircuitService {

    private final CircuitRepository circuitRepository;
    private final UserRepository userRepository;

    public CircuitService(
            CircuitRepository circuitRepository,
            UserRepository userRepository) {
        this.circuitRepository = circuitRepository;
        this.userRepository = userRepository;
    }

    // Afficher tous les circuits ou rechercher
    @Transactional(readOnly = true)
    public List<CircuitDto> findAll(String q) {

        List<Circuit> circuits;

        if (q == null || q.isBlank()) {
            circuits = circuitRepository.findAll(
                    Sort.by(Sort.Direction.ASC, "titre")
            );
        } else {
            circuits = circuitRepository.search(q.trim());
        }

        return circuits.stream()
                .map(CircuitMapper::toDto)
                .toList();
    }

    // Consulter un circuit par son identifiant
    @Transactional(readOnly = true)
    public CircuitDto findById(Long id) {
        return CircuitMapper.toDto(getOrThrow(id));
    }

    // Ajouter un circuit
    public CircuitDto create(CircuitDto dto, String agentEmail) {

        validateDates(dto);

        User agent = userRepository.findByEmail(agentEmail)
                .orElseThrow(() ->
                        new RuntimeException("Agent introuvable")
                );

        Circuit circuit = new Circuit();

        CircuitMapper.copyToEntity(dto, circuit);

        circuit.setAgent(agent);

        Circuit saved = circuitRepository.save(circuit);

        return CircuitMapper.toDto(saved);
    }

    // Modifier un circuit
    public CircuitDto update(Long id, CircuitDto dto) {

        validateDates(dto);

        Circuit circuit = getOrThrow(id);

        CircuitMapper.copyToEntity(dto, circuit);

        Circuit updated = circuitRepository.save(circuit);

        return CircuitMapper.toDto(updated);
    }

    // Supprimer un circuit
    public void delete(Long id) {

        Circuit circuit = getOrThrow(id);

        circuitRepository.delete(circuit);
    }

    // Vérifier l'existence du circuit
    private Circuit getOrThrow(Long id) {
        return circuitRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Circuit introuvable : " + id)
                );
    }

    // Vérifier la cohérence des dates
    private void validateDates(CircuitDto dto) {

        LocalDate depart = dto.getDateDepart();
        LocalDate retour = dto.getDateRetour();

        if (depart != null && retour != null
                && retour.isBefore(depart)) {
            throw new IllegalArgumentException(
                    "La date de retour doit être après la date de départ"
            );
        }
    }
    public CircuitDto getByIdPublic(Long id) {
        Circuit circuit = circuitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Circuit introuvable"));

        return CircuitMapper.toDto(circuit);
    }
}
