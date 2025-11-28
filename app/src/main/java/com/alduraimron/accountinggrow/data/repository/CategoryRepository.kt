package com.alduraimron.accountinggrow.data.repository

import android.util.Log
import com.alduraimron.accountinggrow.data.remote.api.CategoryApi
import com.alduraimron.accountinggrow.domain.model.Category
import com.alduraimron.accountinggrow.domain.model.TransactionType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryApi: CategoryApi
) {
    companion object {
        private const val TAG = "CategoryRepository"
    }

    suspend fun createCategory(
        name: String,
        type: String
    ): Result<Category> {
        return try {
            Log.d(TAG, "Creating category: name=$name, type=$type")

            val requestBody = mapOf(
                "name" to name,
                "type" to type
            )

            val response = categoryApi.createCategory(requestBody)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val category = Category(
                        id = dto.id,
                        name = dto.name,
                        type = when (dto.type) {
                            "INCOME" -> TransactionType.INCOME
                            "EXPENSE" -> TransactionType.EXPENSE
                            else -> TransactionType.EXPENSE
                        }
                    )
                    Log.d(TAG, "Category created successfully: ${category.id}")
                    Result.success(category)
                } else {
                    Result.failure(Exception("Data kategori tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal membuat kategori"
                Log.e(TAG, "Failed to create category: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating category: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun getCategories(type: String? = null): Result<List<Category>> {
        return try {
            Log.d(TAG, "Fetching categories, type: $type")

            val response = categoryApi.getCategories(type)

            if (response.isSuccessful && response.body()?.success == true) {
                val categoryDtos = response.body()?.data ?: emptyList()
                val categories = categoryDtos.map { dto ->
                    Category(
                        id = dto.id,
                        name = dto.name,
                        type = when (dto.type) {
                            "INCOME" -> TransactionType.INCOME
                            "EXPENSE" -> TransactionType.EXPENSE
                            else -> TransactionType.EXPENSE
                        }
                    )
                }
                Log.d(TAG, "Fetched ${categories.size} categories")
                Result.success(categories)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal mengambil kategori"
                Log.e(TAG, "Failed to fetch categories: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching categories: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun deleteCategory(id: String): Result<String> {
        return try {
            Log.d(TAG, "Deleting category: id=$id")

            val response = categoryApi.deleteCategory(id)

            if (response.isSuccessful && response.body()?.success == true) {
                val message = response.body()?.message ?: "Kategori berhasil dihapus"
                Log.d(TAG, "Category deleted successfully")
                Result.success(message)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal menghapus kategori"
                Log.e(TAG, "Failed to delete category: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception deleting category: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }
}