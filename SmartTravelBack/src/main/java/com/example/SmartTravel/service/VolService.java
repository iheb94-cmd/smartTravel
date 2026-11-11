package com.example.SmartTravel.service;

import com.example.SmartTravel.dto.VolDto;
import com.example.SmartTravel.entity.User;
import com.example.SmartTravel.entity.Vol;
import com.example.SmartTravel.enums.StatutOffre;
import com.example.SmartTravel.exception.NotFoundException;
import com.example.SmartTravel.mapper.VolMapper;
import com.example.SmartTravel.repository.UserRepository;
import com.example.SmartTravel.repository.VolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolService {

    private final VolRepository volRepository;
    private final UserRepository userRepository;

    public List<VolDto> findAll(String q) {
        List<Vol> vols = (q == null || q.isBlank())
                ? volRepository.findAll(Sort.by("dateDepart"))
                : volRepository.search(q.trim());
        return vols.stream().map(VolMapper::toDto).toList();
    }

    public VolDto findById(Long id) {
        return VolMapper.toDto(getOrThrow(id));
    }

    public VolDto create(VolDto dto, String agentEmail) {
        validate(dto);
        User agent = userRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new NotFoundException("Agent introuvable"));
        Vol vol = new Vol();
        VolMapper.copyToEntity(dto, vol);
        vol.setAgent(agent);
        return VolMapper.toDto(volRepository.save(vol));
    }

    public VolDto update(Long id, VolDto dto) {
        validate(dto);
        Vol vol = getOrThrow(id);
        VolMapper.copyToEntity(dto, vol); // l'agent d'origine est conservé
        return VolMapper.toDto(volRepository.save(vol));
    }

    public void delete(Long id) {
        volRepository.delete(getOrThrow(id));
    }

    private Vol getOrThrow(Long id) {
        return volRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vol introuvable"));
    }

    // Règles métier : contrôles que les annotations du DTO ne peuvent pas faire
    private void validate(VolDto dto) {
        if (dto.getStatut() == StatutOffre.INACTIF) {
            throw new RuntimeException("Statut invalide pour un vol (disponible, complet ou annulé)");
        }
        if (dto.getVilleDepart().trim().equalsIgnoreCase(dto.getVilleArrivee().trim())) {
            throw new RuntimeException("La ville d'arrivée doit être différente de la ville de départ");
        }
        if (!dto.getDateArrivee().isAfter(dto.getDateDepart())) {
            throw new RuntimeException("La date d'arrivée doit être après la date de départ");
        }
    }
}