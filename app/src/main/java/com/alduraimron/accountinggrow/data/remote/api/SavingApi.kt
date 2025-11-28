package com.alduraimron.accountinggrow.data.remote.api

import com.alduraimron.accountinggrow.data.remote.dto.AddToSavingRequest
import com.alduraimron.accountinggrow.data.remote.dto.ApiResponse
import com.alduraimron.accountinggrow.data.remote.dto.SavingDto
import com.alduraimron.accountinggrow.data.remote.dto.SavingRequest
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
        @Body saving: SavingRequest
    ): Response<ApiResponse<SavingDto>>

    @PUT("savings/{id}")
    suspend fun updateSaving(
        @Path("id") id: String,
        @Body saving: SavingRequest
    ): Response<ApiResponse<SavingDto>>

    @POST("savings/{id}/add")
    suspend fun addToSaving(
        @Path("id") id: String,
        @Body amount: AddToSavingRequest
    ): Response<ApiResponse<SavingDto>>

    @DELETE("savings/{id}")
    suspend fun deleteSaving(
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>
}