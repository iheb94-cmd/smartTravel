package com.isetr.smarttravel.ui.offers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isetr.smarttravel.data.model.Offre
import com.isetr.smarttravel.data.remote.RetrofitClient
import com.isetr.smarttravel.ui.auth.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OffersViewModel : ViewModel() {

    private val _offres = MutableStateFlow<UiState<List<Offre>>>(UiState.Loading)
    private val _query = MutableStateFlow("")

    val offres: StateFlow<UiState<List<Offre>>> =
        combine(_offres, _query) { state, query ->
            if (state is UiState.Success && query.isNotBlank()) {
                val q = query.trim().lowercase()
                UiState.Success(state.data.filter { o ->
                    listOf(
                        o.nomOffre,
                        o.nomVol,
                        o.nomHotel,
                        o.nomCircuit,
                        o.dateDebut,
                        o.dateFin,
                        o.nombrePlaces.toString(),
                        o.prixTotal.toString(),
                        o.statut
                    ).any { it?.lowercase()?.contains(q) == true }
                })
            } else state
        }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Loading)

    init { loadOffres() }

    fun setQuery(q: String) { _query.value = q }

    fun loadOffres() {
        viewModelScope.launch {
            _offres.value = UiState.Loading
            _offres.value = try {
                UiState.Success(RetrofitClient.offerApi.getOffres())
            } catch (e: Exception) {
                UiState.Error("Impossible de charger les offres")
            }
        }
    }
}