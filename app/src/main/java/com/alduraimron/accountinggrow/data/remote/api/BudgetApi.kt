package com.alduraimron.accountinggrow.data.remote.api

import com.alduraimron.accountinggrow.data.remote.dto.ApiResponse
import com.alduraimron.accountinggrow.data.remote.dto.BudgetDto
import com.alduraimron.accountinggrow.data.remote.dto.BudgetRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BudgetApi {

    @GET("budgets")
    suspend fun getBudgets(
        @Query("month") month: Int? = null,
        @Query("year") year: Int? = null,
        @Query("categoryId") categoryId: String? = null
    ): Response<ApiResponse<List<BudgetDto>>>

    @GET("budgets/{id}")
    suspend fun getBudgetById(
        @Path("id") id: String
    ): Response<ApiResponse<BudgetDto>>

    @POST("budgets")
    suspend fun createBudget(
        @Body budget: BudgetRequest
    ): Response<ApiResponse<BudgetDto>>

    @PUT("budgets/{id}")
    suspend fun updateBudget(
        @Path("id") id: String,
        @Body budget: BudgetRequest
    ): Response<ApiResponse<BudgetDto>>

    @DELETE("budgets/{id}")
    suspend fun deleteBudget(
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>
}