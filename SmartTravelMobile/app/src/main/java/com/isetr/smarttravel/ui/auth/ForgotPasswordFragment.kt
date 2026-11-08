package com.isetr.smarttravel.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.isetr.smarttravel.R
import com.isetr.smarttravel.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSendCode.setOnClickListener { submit() }
        binding.tvBack.setOnClickListener { findNavController().popBackStack() }
        observeState()
    }

    private fun submit() {
        val email = binding.etEmail.text.toString().trim()
        binding.tilEmail.error = Validators.email(email)
        if (binding.tilEmail.error == null) viewModel.sendCode(email)
    }

    private fun observeState() {
        viewModel.sendCodeState.observe(viewLifecycleOwner) { state ->
            val loading = state is UiState.Loading
            binding.btnSendCode.isEnabled = !loading
            binding.btnSendCode.text = if (loading) "ENVOI..." else "ENVOYER LE CODE"

            when (state) {
                is UiState.Success -> {
                    viewModel.resetSendCodeState()
                    findNavController().navigate(R.id.action_forgot_to_reset)
                }
                is UiState.Error -> {
                    viewModel.resetSendCodeState()
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                }
                else -> Unit
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}