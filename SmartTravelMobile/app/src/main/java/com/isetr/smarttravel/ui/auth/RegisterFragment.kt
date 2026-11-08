package com.isetr.smarttravel.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.isetr.smarttravel.data.model.RegisterRequest
import com.isetr.smarttravel.databinding.FragmentRegisterBinding
import com.isetr.smarttravel.R

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(requireContext())
    }

    private val countries = listOf(
        "Tunisie", "Algérie", "Maroc", "Libye", "Égypte", "France", "Italie",
        "Allemagne", "Espagne", "Turquie", "Canada", "Autre"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.etPays.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, countries)
        )
        binding.btnRegister.setOnClickListener { submit() }
        binding.tvLogin.setOnClickListener { goToLogin() }
        observeState()
    }

    private fun submit() {
        val nom = binding.etNom.text.toString().trim()
        val prenom = binding.etPrenom.text.toString().trim()
        val tel = binding.etTel.text.toString().trim()
        val pays = binding.etPays.text.toString().trim()
        val age = binding.etAge.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirm = binding.etConfirm.text.toString()

        binding.tilNom.error = Validators.required(nom, "Le nom")
        binding.tilPrenom.error = Validators.required(prenom, "Le prénom")
        binding.tilTel.error = Validators.phone(tel)
        binding.tilPays.error = Validators.required(pays, "Le pays")
        binding.tilAge.error = Validators.age(age)
        binding.tilEmail.error = Validators.email(email)
        binding.tilPassword.error = Validators.password(password)
        binding.tilConfirm.error = Validators.confirmPassword(password, confirm)

        val valid = listOf(
            binding.tilNom, binding.tilPrenom, binding.tilTel, binding.tilPays,
            binding.tilAge, binding.tilEmail, binding.tilPassword, binding.tilConfirm
        ).all { it.error == null }
        if (!valid) return

        viewModel.register(
            RegisterRequest(
                nom = nom,
                prenom = prenom,
                age = age.toInt(),
                pays = pays,
                telephone = tel,
                email = email,
                password = password
            )
        )
    }

    private fun observeState() {
        viewModel.registerState.observe(viewLifecycleOwner) { state ->
            val loading = state is UiState.Loading
            binding.btnRegister.isEnabled = !loading
            binding.btnRegister.text = if (loading) "INSCRIPTION..." else "S'INSCRIRE"

            when (state) {
                is UiState.Success -> {
                    viewModel.resetRegisterState()
                    Toast.makeText(
                        requireContext(), "Compte créé avec succès. Connectez-vous.", Toast.LENGTH_LONG
                    ).show()
                    goToLogin()
                }
                is UiState.Error -> {
                    viewModel.resetRegisterState()
                    if (state.message.contains("email", ignoreCase = true)) {
                        binding.tilEmail.error = state.message
                    } else {
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }
                }
                else -> Unit
            }
        }
    }


    private fun goToLogin() {
        findNavController().navigate(R.id.action_register_to_login)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}