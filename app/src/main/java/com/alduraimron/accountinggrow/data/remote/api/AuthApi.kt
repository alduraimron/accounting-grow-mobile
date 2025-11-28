package com.alduraimron.accountinggrow.data.remote.api

import com.alduraimron.accountinggrow.data.remote.dto.ApiResponse
import com.alduraimron.accountinggrow.data.remote.dto.AuthResponse
import com.alduraimron.accountinggrow.data.remote.dto.LoginRequest
import com.alduraimron.accountinggrow.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<AuthResponse>>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponse>>

    @POST("auth/refresh-token")
    suspend fun refreshToken(
        @Body refreshToken: Map<String, String>
    ): Response<ApiResponse<AuthResponse>>
}