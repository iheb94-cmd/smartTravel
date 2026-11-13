package com.example.SmartTravel.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardStatsDto {

    private long totalOffres;
    private long totalCircuits;
    private long totalHotels;
    private long totalVols;

    private long totalUsers;
    private long totalAgents;
    private long totalVoyageurs;

    private BigDecimal prixMoyenOffre;
    private long placesTotalesOffres;

    private Map<String, Long> offresParStatut;
    private Map<String, Long> circuitsParStatut;
    private Map<String, Long> hotelsParStatut;
    private Map<String, Long> volsParStatut;
    private Map<String, Long> usersParRole;
    private Map<String, Long> circuitsParPays;
    private Map<String, Long> offresParAgent;
    private Map<String, Long> offresParMois;

    private List<ProchainDepart> prochainsDeparts;

    public record ProchainDepart(
            Long idOffre,
            String nomOffre,
            String destination,
            LocalDate dateDebut,
            BigDecimal prixTotal,
            Integer nombrePlaces,
            String statut) {
    }
}