package com.isetr.smarttravel.data.remote

import com.isetr.smarttravel.data.model.Circuit
import com.isetr.smarttravel.data.model.Hotel
import com.isetr.smarttravel.data.model.Offre

import retrofit2.http.GET
import retrofit2.http.Path


interface OfferApi {
    @GET("agent/offres/public")
    suspend fun getOffres(): List<Offre>

    @GET("agent/offres/public/{id}")
    suspend fun getOffre(@Path("id") id: Int): Offre
    @GET("agent/hotels/public/{id}")
    suspend fun getHotel(@Path("id") id: Int): Hotel

    @GET("agent/circuits/public/{id}")
    suspend fun getCircuit(@Path("id") id: Int): Circuit
}