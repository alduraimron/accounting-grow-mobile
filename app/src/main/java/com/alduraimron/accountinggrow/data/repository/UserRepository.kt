package com.alduraimron.accountinggrow.data.repository

import android.util.Log
import com.alduraimron.accountinggrow.data.local.TokenManager
import com.alduraimron.accountinggrow.data.remote.api.UserApi
import com.alduraimron.accountinggrow.domain.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val tokenManager: TokenManager
) {
    companion object {
        private const val TAG = "UserRepository"
    }

    suspend fun getUserProfile(): Result<UserProfile> {
        return try {
            Log.d(TAG, "Fetching user profile")
            val response = userApi.getUserProfile()

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val profile = UserProfile(
                        id = dto.id,
                        username = dto.username,
                        email = dto.email,
                        firstName = dto.firstName,
                        lastName = dto.lastName,
                        phoneNumber = dto.phoneNumber,
                        bio = dto.bio,
                        createdAt = dto.createdAt ?: "",
                        updatedAt = dto.updatedAt ?: ""
                    )
                    Result.success(profile)
                } else {
                    Result.failure(Exception("Data profil tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal mengambil profil"
                Log.e(TAG, "Failed to fetch profile: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching profile: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun updateUserProfile(
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        bio: String?
    ): Result<UserProfile> {
        return try {
            Log.d(TAG, "Updating user profile")

            val requestBody = buildMap<String, String> {
                firstName?.let { put("firstName", it) }
                lastName?.let { put("lastName", it) }
                phoneNumber?.let { put("phoneNumber", it) }
                bio?.let { put("bio", it) }
            }

            val response = userApi.updateUserProfile(requestBody)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val profile = UserProfile(
                        id = dto.id,
                        username = dto.username,
                        email = dto.email,
                        firstName = dto.firstName,
                        lastName = dto.lastName,
                        phoneNumber = dto.phoneNumber,
                        bio = dto.bio,
                        createdAt = dto.createdAt ?: "",
                        updatedAt = dto.updatedAt ?: ""
                    )
                    Result.success(profile)
                } else {
                    Result.failure(Exception("Data profil tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal memperbarui profil"
                Log.e(TAG, "Failed to update profile: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception updating profile: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun logout(): Result<String> {
        return try {
            Log.d(TAG, "Logging out")
            val response = userApi.logout()

            if (response.isSuccessful && response.body()?.success == true) {
                tokenManager.clearTokens()
                Result.success("Logout berhasil")
            } else {
                // Still clear local tokens even if API call fails
                tokenManager.clearTokens()
                Result.success("Logout berhasil")
            }
        } catch (e: Exception) {
            // Still clear local tokens even on exception
            tokenManager.clearTokens()
            Log.e(TAG, "Exception during logout: ${e.message}", e)
            Result.success("Logout berhasil")
        }
    }
}