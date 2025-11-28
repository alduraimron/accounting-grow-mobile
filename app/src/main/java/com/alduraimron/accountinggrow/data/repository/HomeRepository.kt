package com.alduraimron.accountinggrow.data.repository

import android.util.Log
import com.alduraimron.accountinggrow.data.remote.api.CategoryApi
import com.alduraimron.accountinggrow.data.remote.api.TransactionApi
import com.alduraimron.accountinggrow.data.remote.api.UserApi
import com.alduraimron.accountinggrow.domain.model.Category
import com.alduraimron.accountinggrow.domain.model.Transaction
import com.alduraimron.accountinggrow.domain.model.TransactionSummary
import com.alduraimron.accountinggrow.domain.model.TransactionType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val transactionApi: TransactionApi,
    private val categoryApi: CategoryApi,
    private val userApi: UserApi
) {
    companion object {
        private const val TAG = "HomeRepository"
    }

    suspend fun getTransactionSummary(): Result<TransactionSummary> {
        return try {
            Log.d(TAG, "Fetching transaction summary")
            val response = transactionApi.getTransactionSummary()

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val summary = TransactionSummary(
                        totalIncome = dto.totalIncome,
                        totalExpense = dto.totalExpense,
                        balance = dto.balance,
                        transactionCount = dto.transactionCount
                    )
                    Log.d(TAG, "Summary fetched: $summary")
                    Result.success(summary)
                } else {
                    Result.failure(Exception("Data summary tidak tersedia"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal mengambil summary"
                Log.e(TAG, "Failed to fetch summary: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching summary: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun getRecentTransactions(limit: Int = 5): Result<List<Transaction>> {
        return try {
            Log.d(TAG, "Fetching recent transactions, limit: $limit")
            val response = transactionApi.getTransactions(page = 1, limit = limit)

            if (response.isSuccessful && response.body()?.success == true) {
                val transactionDtos = response.body()?.data?.transactions ?: emptyList()
                val transactions = transactionDtos.map { dto ->
                    Transaction(
                        id = dto.id,
                        userId = dto.userId,
                        categoryId = dto.categoryId,
                        type = when (dto.type) {
                            "INCOME" -> TransactionType.INCOME
                            "EXPENSE" -> TransactionType.EXPENSE
                            else -> TransactionType.EXPENSE
                        },
                        nominal = dto.nominal,
                        description = dto.description,
                        date = dto.date,
                        createdAt = dto.createdAt,
                        category = dto.category?.let { catDto ->
                            Category(
                                id = catDto.id,
                                name = catDto.name,
                                type = when (catDto.type) {
                                    "INCOME" -> TransactionType.INCOME
                                    "EXPENSE" -> TransactionType.EXPENSE
                                    else -> TransactionType.EXPENSE
                                }
                            )
                        }
                    )
                }
                Log.d(TAG, "Fetched ${transactions.size} transactions")
                Result.success(transactions)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal mengambil transaksi"
                Log.e(TAG, "Failed to fetch transactions: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching transactions: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            Log.d(TAG, "Fetching categories")
            val response = categoryApi.getCategories()

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

    suspend fun getUserProfile(): Result<String> {
        return try {
            Log.d(TAG, "Fetching user profile")
            val response = userApi.getUserProfile()

            if (response.isSuccessful && response.body()?.success == true) {
                val user = response.body()?.data
                if (user != null) {
                    Log.d(TAG, "User profile fetched: ${user.username}")
                    Result.success(user.username)
                } else {
                    Result.failure(Exception("Data user tidak tersedia"))
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
}