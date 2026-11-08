package com.isetr.smarttravel.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.isetr.smarttravel.R
import com.isetr.smarttravel.data.local.SessionManager
import com.isetr.smarttravel.databinding.FragmentHomeBinding
import com.isetr.smarttravel.ui.auth.AuthViewModel
import com.isetr.smarttravel.ui.auth.AuthViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val name = SessionManager(requireContext()).getFullName()
        binding.tvWelcome.text = "Bienvenue $name"

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_home_to_offers)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}