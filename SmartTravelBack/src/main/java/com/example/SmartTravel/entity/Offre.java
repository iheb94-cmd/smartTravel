
package com.example.SmartTravel.entity;

import com.example.SmartTravel.enums.StatutOffre;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "offres")
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_offre")
    private Long idOffre;

    @Column(name = "nom_offre", nullable = false, length = 150)
    private String nomOffre;

    // Un vol obligatoire par offre
    @ManyToOne(optional = false)
    @JoinColumn(name = "vol_id", nullable = false)
    private Vol vol;

    // Un hôtel obligatoire par offre
    @ManyToOne(optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    // Un circuit obligatoire par offre
    @ManyToOne(optional = false)
    @JoinColumn(name = "circuit_id", nullable = false)
    private Circuit circuit;

    @Column(name = "prix_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixTotal;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "nombre_places", nullable = false)
    private Integer nombrePlaces;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutOffre statut;

    // Agent qui a créé l'offre
    @ManyToOne(optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;
}
