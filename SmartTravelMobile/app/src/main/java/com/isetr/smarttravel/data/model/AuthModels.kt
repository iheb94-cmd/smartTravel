package com.isetr.smarttravel.data.model

data class RegisterRequest(
    val nom: String,
    val prenom: String,
    val age: Int?,
    val pays: String,
    val telephone: String,
    val email: String,
    val password: String
)

data class LoginRequest(val email: String, val password: String)

data class UserDto(
    val id: Long,
    val nom: String?,
    val prenom: String?,
    val email: String,
    val telephone: String?,
    val age: Int?,
    val pays: String?,
    val role: String
)

data class LoginResponse(val token: String, val user: UserDto)

data class EmailRequest(val email: String)

data class UpdatePasswordRequest(
    val email: String,
    val confirmCode: String,
    val newPassword: String
)

data class ErrorResponse(val message: String?)