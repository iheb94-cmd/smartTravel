package com.isetr.smarttravel.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isetr.smarttravel.data.model.LoginResponse
import com.isetr.smarttravel.data.model.RegisterRequest
import com.isetr.smarttravel.data.model.UserDto
import com.isetr.smarttravel.data.repository.AuthRepository
import com.isetr.smarttravel.data.repository.AuthResult
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<LoginResponse>>(UiState.Idle)
    val loginState: LiveData<UiState<LoginResponse>> = _loginState

    private val _registerState = MutableLiveData<UiState<UserDto>>(UiState.Idle)
    val registerState: LiveData<UiState<UserDto>> = _registerState

    private val _sendCodeState = MutableLiveData<UiState<Boolean>>(UiState.Idle)
    val sendCodeState: LiveData<UiState<Boolean>> = _sendCodeState

    private val _resetState = MutableLiveData<UiState<Boolean>>(UiState.Idle)
    val resetState: LiveData<UiState<Boolean>> = _resetState

    // Email saisi à l'étape 1 de la réinitialisation, réutilisé à l'étape 2
    var resetEmail: String = ""
        private set

    fun login(email: String, password: String) {
        if (_loginState.value is UiState.Loading) return
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            _loginState.value = repository.login(email.trim().lowercase(), password).toUiState()
        }
    }

    fun register(request: RegisterRequest) {
        if (_registerState.value is UiState.Loading) return
        _registerState.value = UiState.Loading
        viewModelScope.launch {
            _registerState.value = repository.register(
                request.copy(email = request.email.trim().lowercase())
            ).toUiState()
        }
    }

    fun sendCode(email: String) {
        if (_sendCodeState.value is UiState.Loading) return
        resetEmail = email.trim().lowercase()
        _sendCodeState.value = UiState.Loading
        viewModelScope.launch {
            _sendCodeState.value = repository.sendCode(resetEmail).toUiState()
        }
    }

    fun updatePassword(code: String, newPassword: String) {
        if (_resetState.value is UiState.Loading) return
        _resetState.value = UiState.Loading
        viewModelScope.launch {
            _resetState.value = repository.updatePassword(resetEmail, code.trim(), newPassword).toUiState()
        }
    }

    // À appeler après avoir traité un succès/une erreur (évite de rejouer la navigation)
    fun resetLoginState() { _loginState.value = UiState.Idle }
    fun resetRegisterState() { _registerState.value = UiState.Idle }
    fun resetSendCodeState() { _sendCodeState.value = UiState.Idle }
    fun resetResetState() { _resetState.value = UiState.Idle }

    fun isLoggedIn() = repository.isLoggedIn()
    fun logout() = repository.logout()

    private fun <T> AuthResult<T>.toUiState(): UiState<T> = when (this) {
        is AuthResult.Success -> UiState.Success(data)
        is AuthResult.Error -> UiState.Error(message)
    }

}