
package com.example.SmartTravel.service;

import com.example.SmartTravel.dto.OffreDto;
import com.example.SmartTravel.entity.*;
import com.example.SmartTravel.mapper.OffreMapper;
import com.example.SmartTravel.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class OffreService {

    private final OffreRepository offreRepository;
    private final VolRepository volRepository;
    private final HotelRepository hotelRepository;
    private final CircuitRepository circuitRepository;
    private final UserRepository userRepository;

    public OffreService(
            OffreRepository offreRepository,
            VolRepository volRepository,
            HotelRepository hotelRepository,
            CircuitRepository circuitRepository,
            UserRepository userRepository) {

        this.offreRepository = offreRepository;
        this.volRepository = volRepository;
        this.hotelRepository = hotelRepository;
        this.circuitRepository = circuitRepository;
        this.userRepository = userRepository;
    }

    // 1. Afficher toutes les offres de l'agent connecté
    public List<OffreDto> getAll(String email) {
        return offreRepository.findAll()
                .stream()
                .map(OffreMapper::toDto)
                .toList();
    }

    // 2. Afficher une offre par son ID
    public OffreDto getById(Long id, String email) {

        Offre offre = getOffreAgent(id, email);

        return OffreMapper.toDto(offre);
    }

    // 3. Ajouter une offre
    public OffreDto create(OffreDto dto, String email) {

        Offre offre = new Offre();

        remplirOffre(offre, dto, email);

        return OffreMapper.toDto(offreRepository.save(offre));
    }

    // 4. Modifier une offre
    public OffreDto update(Long id, OffreDto dto, String email) {

        Offre offre = getOffreAgent(id, email);

        remplirOffre(offre, dto, email);

        return OffreMapper.toDto(offreRepository.save(offre));
    }

    // 5. Supprimer une offre
    public void delete(Long id, String email) {

        Offre offre = getOffreAgent(id, email);

        // Supprime uniquement l'offre, jamais ses composants
        offreRepository.delete(offre);
    }

    // Méthode pour remplir les informations de l'offre
    private void remplirOffre(
            Offre offre,
            OffreDto dto,
            String email) {

        if (dto.getDateFin().isBefore(dto.getDateDebut())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La date de fin doit être après la date de début"
            );
        }

        Vol vol = volRepository.findById(dto.getVolId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vol introuvable"));

        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Hôtel introuvable"));

        Circuit circuit = circuitRepository.findById(dto.getCircuitId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Circuit introuvable"));

        User agent = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Agent introuvable"));

        offre.setNomOffre(dto.getNomOffre());
        offre.setVol(vol);
        offre.setHotel(hotel);
        offre.setCircuit(circuit);
        offre.setPrixTotal(dto.getPrixTotal());
        offre.setDateDebut(dto.getDateDebut());
        offre.setDateFin(dto.getDateFin());
        offre.setNombrePlaces(dto.getNombrePlaces());
        offre.setStatut(dto.getStatut());
        if (offre.getIdOffre() == null) {
            offre.setAgent(agent);
        }
    }

    // Vérifier que l'offre appartient à l'agent connecté
    private Offre getOffreAgent(Long id, String email) {
        return offreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Offre introuvable"
                ));
    }
    // Public (visiteur) : toutes les offres
    public List<OffreDto> getAllPublic() {
        return offreRepository.findAll()
                .stream()
                .map(OffreMapper::toDto)
                .toList();
    }

    // Public (visiteur) : une offre par ID
    public OffreDto getByIdPublic(Long id) {
        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Offre introuvable"));

        return OffreMapper.toDto(offre);
    }
}
