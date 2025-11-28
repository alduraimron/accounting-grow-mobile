package com.alduraimron.accountinggrow.data.repository

import android.util.Log
import com.alduraimron.accountinggrow.data.remote.api.TransactionApi
import com.alduraimron.accountinggrow.domain.model.Category
import com.alduraimron.accountinggrow.domain.model.Transaction
import com.alduraimron.accountinggrow.domain.model.TransactionType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionApi: TransactionApi
) {
    companion object {
        private const val TAG = "TransactionRepository"
    }

    suspend fun createTransaction(
        categoryId: String,
        type: String,
        nominal: Double,
        description: String?,
        date: String
    ): Result<Transaction> {
        return try {
            Log.d(TAG, "Creating transaction: categoryId=$categoryId, type=$type, nominal=$nominal")

            val requestBody = buildMap<String, Any> {
                put("categoryId", categoryId)
                put("type", type)
                put("nominal", nominal)
                put("date", date)
                description?.let { put("description", it) }
            }

            val response = transactionApi.createTransaction(requestBody)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val transaction = Transaction(
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
                    Log.d(TAG, "Transaction created successfully: ${transaction.id}")
                    Result.success(transaction)
                } else {
                    Result.failure(Exception("Data transaksi tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal membuat transaksi"
                Log.e(TAG, "Failed to create transaction: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating transaction: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun getAllTransactions(
        page: Int = 1,
        limit: Int = 20,
        type: String? = null
    ): Result<List<Transaction>> {
        return try {
            Log.d(TAG, "Fetching transactions: page=$page, limit=$limit, type=$type")

            val response = transactionApi.getTransactions(
                page = page,
                limit = limit,
                type = type
            )

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

    suspend fun deleteTransaction(id: String): Result<String> {
        return try {
            Log.d(TAG, "Deleting transaction: id=$id")

            val response = transactionApi.deleteTransaction(id)

            if (response.isSuccessful && response.body()?.success == true) {
                val message = response.body()?.message ?: "Transaksi berhasil dihapus"
                Log.d(TAG, "Transaction deleted successfully")
                Result.success(message)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal menghapus transaksi"
                Log.e(TAG, "Failed to delete transaction: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception deleting transaction: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }
}