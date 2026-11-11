
package com.example.SmartTravel.entity;

import com.example.SmartTravel.enums.StatutOffre;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hotel")
    private Long idHotel;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(nullable = false, length = 100)
    private String pays;

    @Column(nullable = false, length = 100)
    private String ville;

    @Column(length = 255)
    private String adresse;

    @Column(nullable = false)
    private Integer categorie;

    @Column(name = "prix_par_nuit",
            nullable = false, precision = 10, scale = 2)
    private BigDecimal prixParNuit;

    @Column(name = "type_hebergement", length = 50)
    private String typeHebergement;

    private Integer capacite;

    @Column(name = "nombre_chambres")
    private Integer nombreChambres;

    @Column(columnDefinition = "TEXT")
    private String services;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutOffre statut;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;
}
