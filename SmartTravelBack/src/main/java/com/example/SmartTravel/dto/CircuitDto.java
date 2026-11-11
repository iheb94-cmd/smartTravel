
package com.example.SmartTravel.dto;

import com.example.SmartTravel.enums.StatutOffre;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CircuitDto {

    private Long idCircuit;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 150)
    private String titre;

    @NotBlank(message = "Le pays est obligatoire")
    @Size(max = 100)
    private String pays;

    @NotBlank(message = "La ville de départ est obligatoire")
    @Size(max = 100)
    private String villeDepart;

    private String destinations;

    @NotNull(message = "La date de départ est obligatoire")
    private LocalDate dateDepart;

    @NotNull(message = "La date de retour est obligatoire")
    private LocalDate dateRetour;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée doit être supérieure à zéro")
    private Integer duree;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être positif")
    private BigDecimal prix;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 1, message = "Le nombre de places doit être supérieur à zéro")
    private Integer nombrePlaces;

    private String typeCircuit;

    private String programme;

    private String servicesInclus;

    private String description;

    private String image;

    @NotNull(message = "Le statut est obligatoire")
    private StatutOffre statut;

    private Long agentId;

    private String agentNom;

    public Long getIdCircuit() {
        return idCircuit;
    }

    public void setIdCircuit(Long idCircuit) {
        this.idCircuit = idCircuit;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getDestinations() {
        return destinations;
    }

    public void setDestinations(String destinations) {
        this.destinations = destinations;
    }

    public LocalDate getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Integer getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(Integer nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public String getTypeCircuit() {
        return typeCircuit;
    }

    public void setTypeCircuit(String typeCircuit) {
        this.typeCircuit = typeCircuit;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public String getServicesInclus() {
        return servicesInclus;
    }

    public void setServicesInclus(String servicesInclus) {
        this.servicesInclus = servicesInclus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public StatutOffre getStatut() {
        return statut;
    }

    public void setStatut(StatutOffre statut) {
        this.statut = statut;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentNom() {
        return agentNom;
    }

    public void setAgentNom(String agentNom) {
        this.agentNom = agentNom;
    }
}
