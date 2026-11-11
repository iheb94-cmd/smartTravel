
package com.example.SmartTravel.dto;

import com.example.SmartTravel.enums.StatutOffre;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelDto {

    private Long idHotel; // Lecture seule

    @NotBlank(message = "Le nom de l'hôtel est obligatoire")
    @Size(max = 150, message = "Le nom ne dépasse pas 150 caractères")
    private String nom;

    @NotBlank(message = "Le pays est obligatoire")
    @Size(max = 100)
    private String pays;

    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 100)
    private String ville;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 255)
    private String adresse;

    @NotNull(message = "La catégorie est obligatoire")
    @Min(value = 1, message = "La catégorie minimale est 1 étoile")
    @Max(value = 5, message = "La catégorie maximale est 5 étoiles")
    private Integer categorie;

    @NotNull(message = "Le prix par nuit est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Le prix par nuit doit être supérieur à 0")
    private BigDecimal prixParNuit;

    @NotBlank(message = "Le type d'hébergement est obligatoire")
    @Size(max = 50)
    private String typeHebergement;

    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité doit être au moins 1")
    private Integer capacite;

    @NotNull(message = "Le nombre de chambres est obligatoire")
    @Min(value = 0, message = "Le nombre de chambres ne peut pas être négatif")
    private Integer nombreChambres;

    private String services;

    private String description;

    private String image;

    @NotNull(message = "Le statut est obligatoire")
    private StatutOffre statut;

    private Long agentId;       // Lecture seule
    private String agentNom;    // Lecture seule
}
