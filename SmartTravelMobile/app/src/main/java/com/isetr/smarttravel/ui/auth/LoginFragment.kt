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
import com.isetr.smarttravel.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnLogin.setOnClickListener { submit() }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
        binding.tvForgot.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgot)
        }
        observeState()
    }

    private fun submit() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        binding.tilEmail.error = Validators.email(email)
        binding.tilPassword.error = Validators.required(password, "Le mot de passe")

        if (binding.tilEmail.error == null && binding.tilPassword.error == null) {
            viewModel.login(email, password)
        }
    }

    private fun observeState() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            val loading = state is UiState.Loading
            binding.btnLogin.isEnabled = !loading
            binding.btnLogin.text = if (loading) "CONNEXION..." else "SE CONNECTER"

            when (state) {
                is UiState.Success -> {
                    viewModel.resetLoginState()
                    findNavController().navigate(R.id.action_login_to_home)
                }
                is UiState.Error -> {
                    viewModel.resetLoginState()
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