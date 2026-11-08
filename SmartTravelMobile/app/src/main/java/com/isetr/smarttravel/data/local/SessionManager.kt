package com.isetr.smarttravel.data.local

import android.content.Context
import com.isetr.smarttravel.data.model.UserDto
import android.util.Base64
import org.json.JSONObject

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("smarttravel_session", Context.MODE_PRIVATE)

    fun saveSession(token: String, user: UserDto) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putLong(KEY_ID, user.id)
            .putString(KEY_NOM, user.nom)
            .putString(KEY_PRENOM, user.prenom)
            .putString(KEY_EMAIL, user.email)
            .putString(KEY_ROLE, user.role)
            .apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getFullName(): String =
        "${prefs.getString(KEY_PRENOM, "") ?: ""} ${prefs.getString(KEY_NOM, "") ?: ""}".trim()

    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

    //Objectif : si l’utilisateur est déjà connecté, l’app s’ouvre directement sur l’accueil. Sinon, elle s’ouvre sur les offres. Le token JWT du backend expire au bout de 24 h, donc on vérifie aussi sa date d’expiration (critère II.2 du prof)
    fun isLoggedIn(): Boolean {
        val token = getToken() ?: return false
        if (isExpired(token)) {
            clear()
            return false
        }
        return true
    }

    // Lit le champ "exp" (en secondes) du JWT, sans appeler le serveur
    private fun isExpired(token: String): Boolean = try {
        val payload = token.split(".")[1]
        val json = String(
            Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        )
        val exp = JSONObject(json).getLong("exp")
        System.currentTimeMillis() / 1000 >= exp
    } catch (e: Exception) {
        true // token illisible : on considère la session invalide
    }

    fun clear() = prefs.edit().clear().apply()

    private companion object {
        const val KEY_TOKEN = "token"
        const val KEY_ID = "user_id"
        const val KEY_NOM = "nom"
        const val KEY_PRENOM = "prenom"
        const val KEY_EMAIL = "email"
        const val KEY_ROLE = "role"
    }
}