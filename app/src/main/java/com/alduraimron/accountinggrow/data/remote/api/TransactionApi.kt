package com.alduraimron.accountinggrow.data.remote.api

import com.alduraimron.accountinggrow.data.remote.dto.ApiResponse
import com.alduraimron.accountinggrow.data.remote.dto.TransactionDto
import com.alduraimron.accountinggrow.data.remote.dto.TransactionSummaryDto
import com.alduraimron.accountinggrow.data.remote.dto.TransactionsResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionApi {

    @GET("transactions/summary")
    suspend fun getTransactionSummary(
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<ApiResponse<TransactionSummaryDto>>

    @GET("transactions")
    suspend fun getTransactions(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("type") type: String? = null,
        @Query("categoryId") categoryId: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<ApiResponse<TransactionsResponseDto>>

    @POST("transactions")
    suspend fun createTransaction(
        @Body transaction: Map<String, Any>
    ): Response<ApiResponse<TransactionDto>>

    @GET("transactions/{id}")
    suspend fun getTransactionById(
        @Path("id") id: String
    ): Response<ApiResponse<TransactionDto>>

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: String,
        @Body transaction: Map<String, Any>
    ): Response<ApiResponse<TransactionDto>>

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>
}