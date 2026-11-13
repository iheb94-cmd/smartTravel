package com.isetr.smarttravel.ui.offers

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.isetr.smarttravel.R
import com.isetr.smarttravel.data.local.SessionManager
import com.isetr.smarttravel.databinding.FragmentOffreListBinding
import com.isetr.smarttravel.ui.auth.UiState
import kotlinx.coroutines.launch

class OffersFragment : Fragment(R.layout.fragment_offre_list) {

    private val viewModel: OffersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentOffreListBinding.bind(view)

        val adapter = OffreAdapter(
            onDetails = { offre ->
                val args = Bundle().apply { putInt("offreId", offre.idOffre) }
                findNavController().navigate(R.id.action_offers_to_details, args)
            },
            onReserver = { offre ->
                if (SessionManager(requireContext()).isLoggedIn()) {
                    Toast.makeText(requireContext(), "Réserver : ${offre.nomOffre}", Toast.LENGTH_SHORT).show()
                    // TODO: appel API réservation
                } else {
                    Toast.makeText(requireContext(), "Connectez-vous pour réserver", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        )
        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = adapter

        // 1) el recherche
        b.etSearch.doAfterTextChanged { viewModel.setQuery(it?.toString().orEmpty()) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.offres.collect { state ->
                    b.progress.isVisible = state is UiState.Loading
                    // 2) 7adhfet el ligne "b.error.isVisible = state is UiState.Error"
                    when (state) {
                        is UiState.Success -> {
                            adapter.submitList(state.data)
                            b.error.isVisible = state.data.isEmpty()
                            if (state.data.isEmpty()) b.error.text = "Aucune offre trouvée"
                        }
                        is UiState.Error -> {
                            b.error.isVisible = true
                            b.error.text = state.message
                        }
                        else -> b.error.isVisible = false
                    }
                }
            }
        }
    }
}