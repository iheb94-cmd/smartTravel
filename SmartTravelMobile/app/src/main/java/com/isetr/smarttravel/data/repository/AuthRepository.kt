package com.isetr.smarttravel.data.repository

import com.google.gson.Gson
import com.isetr.smarttravel.data.local.SessionManager
import com.isetr.smarttravel.data.model.*
import com.isetr.smarttravel.data.remote.AuthApi
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class AuthRepository(
    private val api: AuthApi,
    private val session: SessionManager
) {

    suspend fun register(request: RegisterRequest): AuthResult<UserDto> =
        safeCall("Inscription impossible") { api.register(request) }

    suspend fun login(email: String, password: String): AuthResult<LoginResponse> {
        val result = safeCall("Connexion impossible") {
            api.login(LoginRequest(email, password))
        }
        return when (result) {
            is AuthResult.Success -> {
                session.saveSession(result.data.token, result.data.user)
                result
            }
            is AuthResult.Error -> {
                // Le backend distingue "User not found" / "Invalid password" :
                // on affiche le même message pour ne pas révéler quels emails existent.
                if (result.message == "User not found" || result.message == "Invalid password")
                    AuthResult.Error("Email ou mot de passe incorrect")
                else result
            }
        }
    }

    suspend fun sendCode(email: String): AuthResult<Boolean> =
        safeCall("Envoi du code impossible") { api.sendCode(EmailRequest(email)) }

    suspend fun updatePassword(email: String, code: String, newPassword: String): AuthResult<Boolean> =
        safeCall("Modification impossible") {
            api.updatePassword(UpdatePasswordRequest(email, code, newPassword))
        }

    fun logout() = session.clear()

    fun isLoggedIn(): Boolean = session.isLoggedIn()

    private suspend fun <T> safeCall(
        defaultError: String,
        call: suspend () -> Response<T>
    ): AuthResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) AuthResult.Success(body)
                else AuthResult.Error("Réponse vide du serveur")
            } else {
                AuthResult.Error(parseError(response) ?: defaultError)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            AuthResult.Error("Impossible de joindre le serveur. Vérifiez votre connexion.")
        } catch (e: Exception) {
            AuthResult.Error("Une erreur inattendue est survenue")
        }
    }

    private fun <T> parseError(response: Response<T>): String? = try {
        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)?.message
    } catch (e: Exception) {
        null
    }
}