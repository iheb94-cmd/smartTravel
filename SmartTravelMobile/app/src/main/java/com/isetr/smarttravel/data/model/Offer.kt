package com.isetr.smarttravel.data.model

data class Offre(
    val idOffre: Int,
    val agentId: Int,
    val agentNom: String?,
    val volId: Int,
    val hotelId: Int,
    val circuitId: Int,
    val nomOffre: String,
    val nomVol: String?,
    val nomHotel: String?,
    val nomCircuit: String?,
    val dateDebut: String,
    val dateFin: String,
    val nombrePlaces: Int,
    val prixTotal: Double,
    val statut: String
)
data class Hotel(
    val idHotel: Int,
    val nom: String?,
    val pays: String?,
    val ville: String?,
    val adresse: String?,
    val categorie: Int?,
    val prixParNuit: Double?,
    val typeHebergement: String?,
    val capacite: Int?,
    val nombreChambres: Int?,
    val services: String?,
    val description: String?,
    val image: String?
)

data class Circuit(
    val idCircuit: Int,
    val titre: String?,
    val pays: String?,
    val villeDepart: String?,
    val destinations: String?,
    val dateDepart: String?,
    val dateRetour: String?,
    val duree: Int?,
    val prix: Double?,
    val nombrePlaces: Int?,
    val typeCircuit: String?,
    val programme: String?,
    val servicesInclus: String?,
    val description: String?,
    val image: String?
)