package com.alduraimron.accountinggrow.data.repository

import android.util.Log
import com.alduraimron.accountinggrow.data.remote.api.BudgetApi
import com.alduraimron.accountinggrow.domain.model.Budget
import com.alduraimron.accountinggrow.domain.model.BudgetType
import com.alduraimron.accountinggrow.domain.model.Category
import com.alduraimron.accountinggrow.domain.model.TransactionType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepository @Inject constructor(
    private val budgetApi: BudgetApi
) {
    companion object {
        private const val TAG = "BudgetRepository"
    }

    suspend fun getBudgets(month: Int? = null, year: Int? = null): Result<List<Budget>> {
        return try {
            Log.d(TAG, "Fetching budgets: month=$month, year=$year")
            val response = budgetApi.getBudgets(month, year)

            if (response.isSuccessful && response.body()?.success == true) {
                val budgetDtos = response.body()?.data ?: emptyList()
                val budgets = budgetDtos.map { dto ->
                    Budget(
                        id = dto.id,
                        amount = dto.amount,
                        budgetType = when (dto.budgetType) {
                            "DAILY" -> BudgetType.DAILY
                            "WEEKLY" -> BudgetType.WEEKLY
                            "MONTHLY" -> BudgetType.MONTHLY
                            else -> BudgetType.MONTHLY
                        },
                        month = dto.month,
                        year = dto.year,
                        notes = dto.notes,
                        categoryId = dto.categoryId,
                        userId = dto.userId,
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
                        },
                        spent = dto.spent,
                        remaining = dto.remaining,
                        percentage = dto.percentage,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                }
                Log.d(TAG, "Fetched ${budgets.size} budgets")
                Result.success(budgets)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal mengambil anggaran"
                Log.e(TAG, "Failed to fetch budgets: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching budgets: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun createBudget(
        amount: Double,
        budgetType: String,
        month: Int,
        year: Int,
        notes: String?,
        categoryId: String
    ): Result<Budget> {
        return try {
            Log.d(TAG, "Creating budget for category: $categoryId")

            val requestBody = buildMap<String, Any> {
                put("amount", amount)
                put("budgetType", budgetType)
                put("month", month)
                put("year", year)
                notes?.let { put("notes", it) }
                put("categoryId", categoryId)
            }

            val response = budgetApi.createBudget(requestBody)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val budget = Budget(
                        id = dto.id,
                        amount = dto.amount,
                        budgetType = when (dto.budgetType) {
                            "DAILY" -> BudgetType.DAILY
                            "WEEKLY" -> BudgetType.WEEKLY
                            "MONTHLY" -> BudgetType.MONTHLY
                            else -> BudgetType.MONTHLY
                        },
                        month = dto.month,
                        year = dto.year,
                        notes = dto.notes,
                        categoryId = dto.categoryId,
                        userId = dto.userId,
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
                        },
                        spent = dto.spent,
                        remaining = dto.remaining,
                        percentage = dto.percentage,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                    Log.d(TAG, "Budget created successfully")
                    Result.success(budget)
                } else {
                    Result.failure(Exception("Data anggaran tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal membuat anggaran"
                Log.e(TAG, "Failed to create budget: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating budget: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun deleteBudget(id: String): Result<String> {
        return try {
            Log.d(TAG, "Deleting budget: id=$id")

            val response = budgetApi.deleteBudget(id)

            if (response.isSuccessful && response.body()?.success == true) {
                val message = response.body()?.message ?: "Anggaran berhasil dihapus"
                Result.success(message)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal menghapus anggaran"
                Log.e(TAG, "Failed to delete budget: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception deleting budget: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }
}