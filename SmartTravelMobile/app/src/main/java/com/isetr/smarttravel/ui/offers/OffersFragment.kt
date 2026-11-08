package com.isetr.smarttravel.ui.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.isetr.smarttravel.data.model.SampleOffers
import com.isetr.smarttravel.databinding.FragmentOffersBinding
import androidx.navigation.fragment.findNavController
import com.isetr.smarttravel.R

class OffersFragment : Fragment() {

    private var _binding: FragmentOffersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvOffers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOffers.adapter = OffersAdapter(SampleOffers.list) { goToRegister() }
        binding.btnStartTravel.setOnClickListener { goToRegister() }
        binding.tvLogin.setOnClickListener { goToLogin() }
    }


    private fun goToRegister() {
        findNavController().navigate(R.id.registerFragment)
    }

    private fun goToLogin() {
        findNavController().navigate(R.id.loginFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}