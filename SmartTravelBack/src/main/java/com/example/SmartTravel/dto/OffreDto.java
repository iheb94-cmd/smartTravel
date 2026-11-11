
package com.example.SmartTravel.dto;

import com.example.SmartTravel.enums.StatutOffre;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OffreDto {

    private Long idOffre;

    @NotBlank(message = "Le nom de l'offre est obligatoire")
    private String nomOffre;

    @NotNull(message = "Veuillez sélectionner un vol")
    private Long volId;

    @NotNull(message = "Veuillez sélectionner un hôtel")
    private Long hotelId;

    @NotNull(message = "Veuillez sélectionner un circuit")
    private Long circuitId;

    @NotNull(message = "Le prix total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à zéro")
    private BigDecimal prixTotal;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 1, message = "Le nombre de places doit être au minimum 1")
    private Integer nombrePlaces;

    @NotNull(message = "Le statut est obligatoire")
    private StatutOffre statut;

    // Informations retournées pour l'affichage Angular
    private String nomVol;
    private String nomHotel;
    private String nomCircuit;
    private String agentNom;
    private Long agentId;
}
