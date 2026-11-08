package com.isetr.smarttravel.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isetr.smarttravel.data.local.SessionManager
import com.isetr.smarttravel.data.remote.RetrofitClient
import com.isetr.smarttravel.data.repository.AuthRepository

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val repository = AuthRepository(
                RetrofitClient.authApi,
                SessionManager(context.applicationContext)
            )
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel inconnu : ${modelClass.name}")
    }
}