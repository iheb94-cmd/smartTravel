package com.example.SmartTravel.mapper;

import com.example.SmartTravel.dto.VolDto;
import com.example.SmartTravel.entity.Vol;

public class VolMapper {

    public static VolDto toDto(Vol v) {
        VolDto dto = new VolDto();
        dto.setIdVol(v.getIdVol());
        dto.setNumeroVol(v.getNumeroVol());
        dto.setVilleDepart(v.getVilleDepart());
        dto.setVilleArrivee(v.getVilleArrivee());
        dto.setDateDepart(v.getDateDepart());
        dto.setDateArrivee(v.getDateArrivee());
        dto.setCompagnie(v.getCompagnie());
        dto.setPrix(v.getPrix());
        dto.setClasse(v.getClasse());
        dto.setNombrePlaces(v.getNombrePlaces());
        dto.setBagageInclus(v.getBagageInclus());
        dto.setDescription(v.getDescription());
        dto.setImage(v.getImage());
        dto.setStatut(v.getStatut());
        if (v.getAgent() != null) {
            dto.setAgentId(v.getAgent().getId());
            dto.setAgentNom(v.getAgent().getPrenom() + " " + v.getAgent().getNom());
        }
        return dto;
    }

    // Copie les champs modifiables ; l'agent n'est jamais pris dans le DTO
    public static void copyToEntity(VolDto dto, Vol v) {
        v.setNumeroVol(dto.getNumeroVol().trim());
        v.setVilleDepart(dto.getVilleDepart().trim());
        v.setVilleArrivee(dto.getVilleArrivee().trim());
        v.setDateDepart(dto.getDateDepart());
        v.setDateArrivee(dto.getDateArrivee());
        v.setCompagnie(dto.getCompagnie().trim());
        v.setPrix(dto.getPrix());
        v.setClasse(dto.getClasse().trim());
        v.setNombrePlaces(dto.getNombrePlaces());
        v.setBagageInclus(Boolean.TRUE.equals(dto.getBagageInclus()));
        v.setDescription(dto.getDescription());
        v.setImage(dto.getImage());
        v.setStatut(dto.getStatut());
    }
}