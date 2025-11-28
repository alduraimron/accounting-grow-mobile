package com.alduraimron.accountinggrow.data.remote.api

import com.alduraimron.accountinggrow.data.remote.dto.ApiResponse
import com.alduraimron.accountinggrow.data.remote.dto.CategoryDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryApi {

    @GET("categories")
    suspend fun getCategories(
        @Query("type") type: String? = null
    ): Response<ApiResponse<List<CategoryDto>>>

    @POST("categories")
    suspend fun createCategory(
        @Body category: Map<String, Any>
    ): Response<ApiResponse<CategoryDto>>

    @GET("categories/{id}")
    suspend fun getCategoryById(
        @Path("id") id: String
    ): Response<ApiResponse<CategoryDto>>

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: String,
        @Body category: Map<String, Any>
    ): Response<ApiResponse<CategoryDto>>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>
}