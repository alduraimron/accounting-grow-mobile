package com.alduraimron.accountinggrow.data.remote.api

import com.alduraimron.accountinggrow.data.remote.dto.ApiResponse
import com.alduraimron.accountinggrow.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi {

    @GET("users/profile")
    suspend fun getUserProfile(): Response<ApiResponse<UserDto>>

    @PUT("users/profile")
    suspend fun updateUserProfile(
        @Body profile: Map<String, String>
    ): Response<ApiResponse<UserDto>>

    @PUT("users/password")
    suspend fun changePassword(
        @Body passwords: Map<String, String>
    ): Response<ApiResponse<Unit>>

    @POST("users/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @DELETE("users/account")
    suspend fun deleteAccount(): Response<ApiResponse<Unit>>
}