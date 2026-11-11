
package com.example.SmartTravel.mapper;

import com.example.SmartTravel.dto.HotelDto;
import com.example.SmartTravel.entity.Hotel;

public class HotelMapper {

    public static HotelDto toDto(Hotel h) {

        HotelDto dto = new HotelDto();

        dto.setIdHotel(h.getIdHotel());
        dto.setNom(h.getNom());
        dto.setPays(h.getPays());
        dto.setVille(h.getVille());
        dto.setAdresse(h.getAdresse());
        dto.setCategorie(h.getCategorie());
        dto.setPrixParNuit(h.getPrixParNuit());
        dto.setTypeHebergement(h.getTypeHebergement());
        dto.setCapacite(h.getCapacite());
        dto.setNombreChambres(h.getNombreChambres());
        dto.setServices(h.getServices());
        dto.setDescription(h.getDescription());
        dto.setImage(h.getImage());
        dto.setStatut(h.getStatut());

        if (h.getAgent() != null) {
            dto.setAgentId(h.getAgent().getId());
            dto.setAgentNom(
                    h.getAgent().getPrenom() + " " +
                            h.getAgent().getNom()
            );
        }

        return dto;
    }

    // Copie uniquement les champs modifiables
    public static void copyToEntity(HotelDto dto, Hotel h) {

        h.setNom(dto.getNom().trim());
        h.setPays(dto.getPays().trim());
        h.setVille(dto.getVille().trim());
        h.setAdresse(dto.getAdresse().trim());
        h.setCategorie(dto.getCategorie());
        h.setPrixParNuit(dto.getPrixParNuit());
        h.setTypeHebergement(dto.getTypeHebergement().trim());
        h.setCapacite(dto.getCapacite());
        h.setNombreChambres(dto.getNombreChambres());
        h.setServices(dto.getServices());
        h.setDescription(dto.getDescription());
        h.setImage(dto.getImage());
        h.setStatut(dto.getStatut());

    }
}
