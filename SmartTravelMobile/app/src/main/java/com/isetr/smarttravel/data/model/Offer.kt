package com.isetr.smarttravel.data.model

data class Offer(
    val id: Int,
    val type: String,
    val title: String,
    val details: String,
    val price: String
)

object SampleOffers {
    val list = listOf(
        Offer(1, "VOL", "Tunis → Istanbul", "15 déc. • 08:30", "850 DT"),
        Offer(2, "HÔTEL", "Hôtel 4 étoiles", "5 nuits • centre-ville", "720 DT"),
        Offer(3, "CIRCUIT", "Circuit culturel", "5 jours • visites guidées", "280 DT"),
        Offer(4, "VOL", "Tunis → Rome", "20 déc. • 10:15", "620 DT"),
        Offer(5, "VOL", "Tunis → Paris", "22 déc. • 14:00", "740 DT")
    )
}