package com.example.SmartTravel.service;

import com.example.SmartTravel.dto.DashboardStatsDto;
import com.example.SmartTravel.dto.DashboardStatsDto.ProchainDepart;
import com.example.SmartTravel.entity.*;
import com.example.SmartTravel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    // Noms exacts des valeurs de l'enum Role (à adapter si besoin)
    private static final String ROLE_AGENT = "AGENT_AGENCE";
    private static final String ROLE_VOYAGEUR = "VOYAGEUR";

    private final OffreRepository offreRepository;
    private final CircuitRepository circuitRepository;
    private final HotelRepository hotelRepository;
    private final VolRepository volRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto getStats() {
        List<Offre> offres = offreRepository.findAll();
        List<Circuit> circuits = circuitRepository.findAll();
        List<Hotel> hotels = hotelRepository.findAll();
        List<Vol> vols = volRepository.findAll();
        List<User> users = userRepository.findAll();

        BigDecimal prixMoyen = offres.isEmpty()
                ? BigDecimal.ZERO
                : offres.stream()
                .map(Offre::getPrixTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(offres.size()), 2, RoundingMode.HALF_UP);

        long places = offres.stream()
                .mapToLong(o -> o.getNombrePlaces() == null ? 0 : o.getNombrePlaces())
                .sum();

        LocalDate today = LocalDate.now();
        DateTimeFormatter moisFmt = DateTimeFormatter.ofPattern("yyyy-MM");

        List<ProchainDepart> prochains = offres.stream()
                .filter(o -> !o.getDateDebut().isBefore(today))
                .sorted(Comparator.comparing(Offre::getDateDebut))
                .limit(5)
                .map(o -> new ProchainDepart(
                        o.getIdOffre(),
                        o.getNomOffre(),
                        o.getCircuit().getPays(),
                        o.getDateDebut(),
                        o.getPrixTotal(),
                        o.getNombrePlaces(),
                        o.getStatut().name()))
                .toList();

        return DashboardStatsDto.builder()
                .totalOffres(offres.size())
                .totalCircuits(circuits.size())
                .totalHotels(hotels.size())
                .totalVols(vols.size())
                .totalUsers(users.size())
                .totalAgents(countRole(users, ROLE_AGENT))
                .totalVoyageurs(countRole(users, ROLE_VOYAGEUR))
                .prixMoyenOffre(prixMoyen)
                .placesTotalesOffres(places)
                .offresParStatut(countBy(offres, o -> o.getStatut().name()))
                .circuitsParStatut(countBy(circuits, c -> c.getStatut().name()))
                .hotelsParStatut(countBy(hotels, h -> h.getStatut().name()))
                .volsParStatut(countBy(vols, v -> v.getStatut().name()))
                .usersParRole(countBy(users, u -> u.getRole().name()))
                .circuitsParPays(countBy(circuits, Circuit::getPays))
                .offresParAgent(countBy(offres, o -> agentLabel(o.getAgent())))
                .offresParMois(new TreeMap<>(countBy(offres, o -> o.getDateDebut().format(moisFmt))))
                .prochainsDeparts(prochains)
                .build();
    }

    private long countRole(List<User> users, String roleName) {
        return users.stream()
                .filter(u -> u.getRole() != null && u.getRole().name().equalsIgnoreCase(roleName))
                .count();
    }

    private <T> Map<String, Long> countBy(List<T> list, Function<T, String> key) {
        return list.stream().collect(Collectors.groupingBy(
                t -> {
                    String k = key.apply(t);
                    return k == null ? "N/A" : k;
                },
                LinkedHashMap::new,
                Collectors.counting()));
    }

    private String agentLabel(User agent) {
        String prenom = agent.getPrenom() == null ? "" : agent.getPrenom();
        String nom = agent.getNom() == null ? "" : agent.getNom();
        String full = (prenom + " " + nom).trim();
        return full.isEmpty() ? agent.getEmail() : full;
    }
}