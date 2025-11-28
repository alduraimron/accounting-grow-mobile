package com.alduraimron.accountinggrow.data.repository

import android.util.Log
import com.alduraimron.accountinggrow.data.remote.api.ArticleApi
import com.alduraimron.accountinggrow.domain.model.Article
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val articleApi: ArticleApi
) {
    companion object {
        private const val TAG = "ArticleRepository"
    }

    suspend fun getArticles(page: Int = 1, limit: Int = 10): Result<List<Article>> {
        return try {
            Log.d(TAG, "Fetching articles: page=$page, limit=$limit")
            val response = articleApi.getArticles(page, limit)

            if (response.isSuccessful && response.body()?.success == true) {
                val articleDtos = response.body()?.data?.articles ?: emptyList()
                val articles = articleDtos.map { dto ->
                    Article(
                        id = dto.id,
                        title = dto.title,
                        content = dto.content,
                        publishedDate = dto.publishedDate,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                }
                Log.d(TAG, "Fetched ${articles.size} articles")
                Result.success(articles)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal mengambil artikel"
                Log.e(TAG, "Failed to fetch articles: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching articles: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun getArticleById(id: String): Result<Article> {
        return try {
            Log.d(TAG, "Fetching article: id=$id")
            val response = articleApi.getArticleById(id)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val article = Article(
                        id = dto.id,
                        title = dto.title,
                        content = dto.content,
                        publishedDate = dto.publishedDate,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                    Result.success(article)
                } else {
                    Result.failure(Exception("Data artikel tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal mengambil artikel"
                Log.e(TAG, "Failed to fetch article: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching article: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }
}