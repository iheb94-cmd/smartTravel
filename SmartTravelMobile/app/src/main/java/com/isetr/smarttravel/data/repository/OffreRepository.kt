package com.isetr.smarttravel.data.repository

import com.isetr.smarttravel.data.remote.OfferApi
import com.isetr.smarttravel.data.remote.RetrofitClient

class OffreRepository(private val api: OfferApi = RetrofitClient.offerApi) {
    suspend fun getOffres() = api.getOffres()
    suspend fun getOffre(id: Int) = api.getOffre(id)
}
