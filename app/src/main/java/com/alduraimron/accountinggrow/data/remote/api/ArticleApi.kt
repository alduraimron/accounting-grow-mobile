package com.alduraimron.accountinggrow.data.remote.api

import com.alduraimron.accountinggrow.data.remote.dto.ApiResponse
import com.alduraimron.accountinggrow.data.remote.dto.ArticleDto
import com.alduraimron.accountinggrow.data.remote.dto.ArticlesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleApi {

    @GET("articles")
    suspend fun getArticles(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<ArticlesResponseDto>>

    @GET("articles/search")
    suspend fun searchArticles(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<ArticlesResponseDto>>

    @GET("articles/{id}")
    suspend fun getArticleById(
        @Path("id") id: String
    ): Response<ApiResponse<ArticleDto>>
}