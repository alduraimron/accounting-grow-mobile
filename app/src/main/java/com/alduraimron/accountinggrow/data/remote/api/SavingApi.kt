package com.alduraimron.accountinggrow.data.remote.api

import com.alduraimron.accountinggrow.data.remote.dto.ApiResponse
import com.alduraimron.accountinggrow.data.remote.dto.SavingDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SavingApi {

    @GET("savings")
    suspend fun getSavings(): Response<ApiResponse<List<SavingDto>>>

    @GET("savings/{id}")
    suspend fun getSavingById(
        @Path("id") id: String
    ): Response<ApiResponse<SavingDto>>

    @POST("savings")
    suspend fun createSaving(
        @Body saving: Map<String, Any>
    ): Response<ApiResponse<SavingDto>>

    @PUT("savings/{id}")
    suspend fun updateSaving(
        @Path("id") id: String,
        @Body saving: Map<String, Any>
    ): Response<ApiResponse<SavingDto>>

    @POST("savings/{id}/add")
    suspend fun addToSaving(
        @Path("id") id: String,
        @Body amount: Map<String, Double>
    ): Response<ApiResponse<SavingDto>>

    @DELETE("savings/{id}")
    suspend fun deleteSaving(
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>
}