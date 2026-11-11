
package com.example.SmartTravel.mapper;

import com.example.SmartTravel.dto.OffreDto;
import com.example.SmartTravel.entity.Offre;

public class OffreMapper {

    public static OffreDto toDto(Offre offre) {

        OffreDto dto = new OffreDto();

        dto.setIdOffre(offre.getIdOffre());
        dto.setNomOffre(offre.getNomOffre());

        // Identifiants des composants associés
        dto.setVolId(offre.getVol().getIdVol());
        dto.setHotelId(offre.getHotel().getIdHotel());
        dto.setCircuitId(offre.getCircuit().getIdCircuit());

        // Informations principales des composants
        dto.setNomVol(
                offre.getVol().getNumeroVol()
                        + " - "
                        + offre.getVol().getCompagnie()
        );

        dto.setNomHotel(offre.getHotel().getNom());
        dto.setNomCircuit(offre.getCircuit().getTitre());

        // Informations de l'offre
        dto.setPrixTotal(offre.getPrixTotal());
        dto.setDateDebut(offre.getDateDebut());
        dto.setDateFin(offre.getDateFin());
        dto.setNombrePlaces(offre.getNombrePlaces());
        dto.setStatut(offre.getStatut());

        // Informations de l'agent
        if (offre.getAgent() != null) {
            dto.setAgentId(offre.getAgent().getId());
            dto.setAgentNom(
                    offre.getAgent().getPrenom()
                            + " "
                            + offre.getAgent().getNom()
            );
        }

        return dto;
    }
}
