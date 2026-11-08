package com.isetr.smarttravel.data.remote

import com.isetr.smarttravel.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserDto>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("pass/send-code")
    suspend fun sendCode(@Body request: EmailRequest): Response<Boolean>

    @POST("pass/update-password")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): Response<Boolean>
}