package com.example.SmartTravel.dto;

import com.example.SmartTravel.enums.StatutOffre;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VolDto {

    private Long idVol; // lecture seule

    @NotBlank(message = "Le numéro de vol est obligatoire")
    @Size(max = 20, message = "Le numéro de vol ne dépasse pas 20 caractères")
    private String numeroVol;

    @NotBlank(message = "La ville de départ est obligatoire")
    private String villeDepart;

    @NotBlank(message = "La ville d'arrivée est obligatoire")
    private String villeArrivee;

    @NotNull(message = "La date de départ est obligatoire")
    private LocalDateTime dateDepart;

    @NotNull(message = "La date d'arrivée est obligatoire")
    private LocalDateTime dateArrivee;

    @NotBlank(message = "La compagnie est obligatoire")
    private String compagnie;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    private BigDecimal prix;

    @NotBlank(message = "La classe est obligatoire")
    private String classe;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 0, message = "Le nombre de places ne peut pas être négatif")
    private Integer nombrePlaces;

    private Boolean bagageInclus = false;
    private String description;
    private String image;

    @NotNull(message = "Le statut est obligatoire")
    private StatutOffre statut;

    private Long agentId;     // lecture seule
    private String agentNom;  // lecture seule
}