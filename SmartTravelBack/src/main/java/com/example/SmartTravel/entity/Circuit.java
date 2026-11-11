
package com.example.SmartTravel.entity;

import com.example.SmartTravel.enums.StatutOffre;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "circuits")
public class Circuit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_circuit")
    private Long idCircuit;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(nullable = false, length = 100)
    private String pays;

    @Column(name = "ville_depart", nullable = false, length = 100)
    private String villeDepart;

    @Column(columnDefinition = "TEXT")
    private String destinations;

    @Column(name = "date_depart", nullable = false)
    private LocalDate dateDepart;

    @Column(name = "date_retour", nullable = false)
    private LocalDate dateRetour;

    @Column(nullable = false)
    private Integer duree;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(name = "nombre_places", nullable = false)
    private Integer nombrePlaces;

    @Column(name = "type_circuit", length = 50)
    private String typeCircuit;

    @Column(columnDefinition = "TEXT")
    private String programme;

    @Column(name = "services_inclus", columnDefinition = "TEXT")
    private String servicesInclus;

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
