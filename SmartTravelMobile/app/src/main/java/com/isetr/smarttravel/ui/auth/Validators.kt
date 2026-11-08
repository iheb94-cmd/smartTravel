package com.isetr.smarttravel.ui.auth

import android.util.Patterns

object Validators {

    fun required(value: String, label: String): String? =
        if (value.isBlank()) "$label est obligatoire" else null

    fun email(value: String): String? = when {
        value.isBlank() -> "L'email est obligatoire"
        !Patterns.EMAIL_ADDRESS.matcher(value.trim()).matches() -> "Format d'email invalide"
        else -> null
    }

    fun password(value: String): String? = when {
        value.isBlank() -> "Le mot de passe est obligatoire"
        value.length < 6 -> "Au moins 6 caractères"
        else -> null
    }

    fun confirmPassword(password: String, confirm: String): String? =
        if (password != confirm) "Les mots de passe ne correspondent pas" else null

    fun phone(value: String): String? = when {
        value.isBlank() -> "Le téléphone est obligatoire"
        !value.matches(Regex("^\\d{8,15}$")) -> "Numéro invalide (8 à 15 chiffres)"
        else -> null
    }

    fun age(value: String): String? {
        val age = value.toIntOrNull() ?: return "L'âge est obligatoire"
        return if (age !in 12..120) "Âge invalide (12 à 120)" else null
    }

    fun code(value: String): String? =
        if (!value.matches(Regex("^\\d{6}$"))) "Le code contient 6 chiffres" else null
}