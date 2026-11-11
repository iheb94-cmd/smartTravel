
package com.example.SmartTravel.dto;

import com.example.SmartTravel.entity.Circuit;

public class CircuitMapper {

    public static CircuitDto toDto(Circuit c) {

        CircuitDto dto = new CircuitDto();

        dto.setIdCircuit(c.getIdCircuit());
        dto.setTitre(c.getTitre());
        dto.setPays(c.getPays());
        dto.setVilleDepart(c.getVilleDepart());
        dto.setDestinations(c.getDestinations());

        dto.setDateDepart(c.getDateDepart());
        dto.setDateRetour(c.getDateRetour());
        dto.setDuree(c.getDuree());

        dto.setPrix(c.getPrix());
        dto.setNombrePlaces(c.getNombrePlaces());

        dto.setTypeCircuit(c.getTypeCircuit());
        dto.setProgramme(c.getProgramme());
        dto.setServicesInclus(c.getServicesInclus());
        dto.setDescription(c.getDescription());
        dto.setImage(c.getImage());

        dto.setStatut(c.getStatut());

        if (c.getAgent() != null) {
            dto.setAgentId(c.getAgent().getId());

            dto.setAgentNom(
                    c.getAgent().getPrenom() + " " +
                            c.getAgent().getNom()
            );
        }

        return dto;
    }

    public static void copyToEntity(CircuitDto dto, Circuit c) {

        c.setTitre(dto.getTitre());
        c.setPays(dto.getPays());
        c.setVilleDepart(dto.getVilleDepart());
        c.setDestinations(dto.getDestinations());

        c.setDateDepart(dto.getDateDepart());
        c.setDateRetour(dto.getDateRetour());
        c.setDuree(dto.getDuree());

        c.setPrix(dto.getPrix());
        c.setNombrePlaces(dto.getNombrePlaces());

        c.setTypeCircuit(dto.getTypeCircuit());
        c.setProgramme(dto.getProgramme());
        c.setServicesInclus(dto.getServicesInclus());
        c.setDescription(dto.getDescription());
        c.setImage(dto.getImage());

        c.setStatut(dto.getStatut());
    }
}
