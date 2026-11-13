package com.isetr.smarttravel.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.isetr.smarttravel.R
import com.isetr.smarttravel.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnReset.setOnClickListener { submit() }
        binding.tvBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        observeState()
    }

    private fun submit() {
        val code = binding.etCode.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirm = binding.etConfirm.text.toString()

        binding.tilCode.error = Validators.code(code)
        binding.tilPassword.error = Validators.password(password)
        binding.tilConfirm.error = Validators.confirmPassword(password, confirm)

        val valid = listOf(binding.tilCode, binding.tilPassword, binding.tilConfirm)
            .all { it.error == null }
        if (valid) viewModel.updatePassword(code, password)
    }

    private fun observeState() {
        viewModel.resetState.observe(viewLifecycleOwner) { state ->
            val loading = state is UiState.Loading
            binding.btnReset.isEnabled = !loading
            binding.btnReset.text = if (loading) "MODIFICATION..." else "MODIFIER MON MOT DE PASSE"

            when (state) {
                is UiState.Success -> {
                    viewModel.resetResetState()
                    Toast.makeText(
                        requireContext(), "Mot de passe modifié. Connectez-vous.", Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_reset_to_login)
                }
                is UiState.Error -> {
                    viewModel.resetResetState()
                    if (state.message.contains("code", ignoreCase = true)) {
                        binding.tilCode.error = state.message.trim()
                    } else {
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }
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