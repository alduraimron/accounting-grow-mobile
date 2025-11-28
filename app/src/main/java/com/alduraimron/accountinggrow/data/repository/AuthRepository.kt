package com.alduraimron.accountinggrow.data.repository

import com.alduraimron.accountinggrow.data.local.TokenManager
import com.alduraimron.accountinggrow.data.remote.api.AuthApi
import com.alduraimron.accountinggrow.data.remote.dto.LoginRequest
import com.alduraimron.accountinggrow.data.remote.dto.RegisterRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {

    suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = authApi.login(LoginRequest(username, password))

            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()?.data
                if (authResponse != null) {
                    // Save tokens
                    tokenManager.saveTokens(
                        authResponse.accessToken,
                        authResponse.refreshToken
                    )

                    // Save user info
                    tokenManager.saveUserInfo(
                        authResponse.user.id,
                        authResponse.user.username,
                        authResponse.user.email
                    )

                    Result.success(response.body()?.message ?: "Login berhasil")
                } else {
                    Result.failure(Exception("Data tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Login gagal"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun register(username: String, email: String, password: String): Result<String> {
        return try {
            val response = authApi.register(RegisterRequest(username, email, password))

            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.message ?: "Registrasi berhasil")
            } else {
                val errorMessage = response.body()?.message ?: "Registrasi gagal"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    fun logout() {
        tokenManager.clearTokens()
    }

    fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }
}