package com.isetr.smarttravel.ui.offers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.isetr.smarttravel.R
import com.isetr.smarttravel.data.local.SessionManager
import com.isetr.smarttravel.data.remote.RetrofitClient
import com.isetr.smarttravel.databinding.DialogDetailBinding
import com.isetr.smarttravel.databinding.FragmentOffreDetailsBinding
import com.isetr.smarttravel.ui.auth.UiState
import coil.load
import kotlinx.coroutines.launch

class OffreDetailsFragment : Fragment(R.layout.fragment_offre_details) {

    private val viewModel: OffreDetailsViewModel by viewModels()
    private val offreId by lazy { requireArguments().getInt("offreId") }

    private fun imageUrl(path: String?): String? = when {
        path.isNullOrBlank() -> null
        path.startsWith("http") -> path.replace("localhost", "10.0.2.2")
        else -> RetrofitClient.BASE_URL + path.trimStart('/')
    }

    private fun showDetailsDialog(title: String, image: String?, message: String) {
        val d = DialogDetailBinding.inflate(layoutInflater)
        d.tvTitle.text = title
        val url = imageUrl(image)
        if (url != null) {
            d.ivImage.load(url)
        } else {
            (d.ivImage.parent as View).visibility = View.GONE
        }
        d.tvMessage.text = message

        val dialog = AlertDialog.Builder(requireContext())
            .setView(d.root)
            .create()

        d.btnClose.setOnClickListener { dialog.dismiss() }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentOffreDetailsBinding.bind(view)
        viewModel.load(offreId)

        b.btnBackToList.setOnClickListener { findNavController().popBackStack() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.details.collect { state ->
                    b.progress.isVisible = state is UiState.Loading
                    when (state) {
                        is UiState.Success -> {
                            val o = state.data.offre
                            val h = state.data.hotel
                            val c = state.data.circuit

                            b.tvNom.text = o.nomOffre
                            b.tvStatut.text = "Statut : ${o.statut}"
                            b.tvGeneral.text = "Prix : ${o.prixTotal} TND\n" +
                                    "Places : ${o.nombrePlaces}\n" +
                                    "Du ${o.dateDebut} au ${o.dateFin}"
                            b.tvVol.text = "Vol : ${o.nomVol}"

                            // ===== HOTEL =====
                            b.tvHotel.text = "Hôtel : ${o.nomHotel}"
                            b.btnHotelDetails.isVisible = true
                            b.btnHotelDetails.setOnClickListener {
                                if (h == null) {
                                    Toast.makeText(requireContext(), "Détails indisponibles", Toast.LENGTH_SHORT).show()
                                } else {
                                    showDetailsDialog(
                                        h.nom ?: o.nomHotel ?: "Hôtel",
                                        h.image,
                                        "Lieu : ${h.ville ?: "-"}, ${h.pays ?: "-"}\n" +
                                                "Adresse : ${h.adresse ?: "-"}\n" +
                                                "Catégorie : ${h.categorie ?: "-"} étoiles\n" +
                                                "Prix / nuit : ${h.prixParNuit ?: "-"} TND\n" +
                                                "Type : ${h.typeHebergement ?: "-"}\n" +
                                                "Capacité : ${h.capacite ?: "-"}\n" +
                                                "Chambres : ${h.nombreChambres ?: "-"}\n\n" +
                                                "Services :\n${h.services ?: "-"}\n\n" +
                                                "Description :\n${h.description ?: "-"}"
                                    )
                                }
                            }

                            // ===== CIRCUIT =====
                            b.tvCircuit.text = "Circuit : ${o.nomCircuit}"
                            b.btnCircuitDetails.isVisible = true
                            b.btnCircuitDetails.setOnClickListener {
                                if (c == null) {
                                    Toast.makeText(requireContext(), "Détails indisponibles", Toast.LENGTH_SHORT).show()
                                } else {
                                    showDetailsDialog(
                                        c.titre ?: o.nomCircuit ?: "Circuit",
                                        c.image,
                                        "Pays : ${c.pays ?: "-"}\n" +
                                                "Départ : ${c.villeDepart ?: "-"}\n" +
                                                "Du ${c.dateDepart ?: "-"} au ${c.dateRetour ?: "-"}\n" +
                                                "Durée : ${c.duree ?: "-"} jours\n" +
                                                "Prix : ${c.prix ?: "-"} TND\n" +
                                                "Places : ${c.nombrePlaces ?: "-"}\n" +
                                                "Type : ${c.typeCircuit ?: "-"}\n\n" +
                                                "Destinations :\n${c.destinations ?: "-"}\n\n" +
                                                "Programme :\n${c.programme ?: "-"}\n\n" +
                                                "Services inclus :\n${c.servicesInclus ?: "-"}"
                                    )
                                }
                            }

                            b.btnReserver.setOnClickListener {
                                if (SessionManager(requireContext()).isLoggedIn()) {
                                    Toast.makeText(requireContext(), "Réserver : ${o.nomOffre}", Toast.LENGTH_SHORT).show()
                                    // TODO: appel API réservation
                                } else {
                                    Toast.makeText(requireContext(), "Connectez-vous pour réserver", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.loginFragment)
                                }
                            }
                        }
                        is UiState.Error ->
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                        else -> {}
                    }
                }
            }
        }
    }
}