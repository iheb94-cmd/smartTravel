package com.isetr.smarttravel.ui.offers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isetr.smarttravel.data.model.Circuit
import com.isetr.smarttravel.data.model.Hotel
import com.isetr.smarttravel.data.model.Offre
import com.isetr.smarttravel.data.remote.RetrofitClient
import com.isetr.smarttravel.ui.auth.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OffreDetails(
    val offre: Offre,
    val hotel: Hotel?,
    val circuit: Circuit?
)

class OffreDetailsViewModel : ViewModel() {

    private val _details = MutableStateFlow<UiState<OffreDetails>>(UiState.Loading)
    val details: StateFlow<UiState<OffreDetails>> = _details

    fun load(id: Int) {
        viewModelScope.launch {
            _details.value = UiState.Loading
            _details.value = try {
                val api = RetrofitClient.offerApi
                val offre = api.getOffre(id)

                coroutineScope {
                    val hotelDeferred = async {
                        try {
                            api.getHotel(offre.hotelId)
                        } catch (e: Exception) {
                            Log.e("DETAILS", "hotel failed: ${e.message}", e)
                            null
                        }
                    }
                    val circuitDeferred = async {
                        try {
                            api.getCircuit(offre.circuitId)
                        } catch (e: Exception) {
                            Log.e("DETAILS", "circuit failed: ${e.message}", e)
                            null
                        }
                    }
                    UiState.Success(
                        OffreDetails(offre, hotelDeferred.await(), circuitDeferred.await())
                    )
                }
            } catch (e: Exception) {
                Log.e("DETAILS", "offre failed: ${e.message}", e)
                UiState.Error("Impossible de charger les détails")
            }
        }
    }
}