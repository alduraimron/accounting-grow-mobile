package com.alduraimron.accountinggrow.data.repository

import android.util.Log
import com.alduraimron.accountinggrow.data.remote.api.SavingApi
import com.alduraimron.accountinggrow.domain.model.FillingPlan
import com.alduraimron.accountinggrow.domain.model.Saving
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavingRepository @Inject constructor(
    private val savingApi: SavingApi
) {
    companion object {
        private const val TAG = "SavingRepository"
    }

    suspend fun getSavings(): Result<List<Saving>> {
        return try {
            Log.d(TAG, "Fetching savings")
            val response = savingApi.getSavings()

            if (response.isSuccessful && response.body()?.success == true) {
                val savingDtos = response.body()?.data ?: emptyList()
                val savings = savingDtos.map { dto ->
                    Saving(
                        id = dto.id,
                        name = dto.name,
                        targetAmount = dto.targetAmount,
                        currentAmount = dto.currentAmount,
                        fillingPlan = when (dto.fillingPlan) {
                            "DAILY" -> FillingPlan.DAILY
                            "WEEKLY" -> FillingPlan.WEEKLY
                            "MONTHLY" -> FillingPlan.MONTHLY
                            else -> FillingPlan.MONTHLY
                        },
                        progress = dto.progress,
                        userId = dto.userId,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                }
                Log.d(TAG, "Fetched ${savings.size} savings")
                Result.success(savings)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal mengambil tabungan"
                Log.e(TAG, "Failed to fetch savings: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching savings: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun createSaving(
        name: String,
        targetAmount: Double,
        currentAmount: Double,
        fillingPlan: String
    ): Result<Saving> {
        return try {
            Log.d(TAG, "Creating saving: name=$name")

            val requestBody = buildMap<String, Any> {
                put("name", name)
                put("targetAmount", targetAmount)
                put("currentAmount", currentAmount)
                put("fillingPlan", fillingPlan)
            }

            val response = savingApi.createSaving(requestBody)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val saving = Saving(
                        id = dto.id,
                        name = dto.name,
                        targetAmount = dto.targetAmount,
                        currentAmount = dto.currentAmount,
                        fillingPlan = when (dto.fillingPlan) {
                            "DAILY" -> FillingPlan.DAILY
                            "WEEKLY" -> FillingPlan.WEEKLY
                            "MONTHLY" -> FillingPlan.MONTHLY
                            else -> FillingPlan.MONTHLY
                        },
                        progress = dto.progress,
                        userId = dto.userId,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                    Log.d(TAG, "Saving created successfully")
                    Result.success(saving)
                } else {
                    Result.failure(Exception("Data tabungan tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal membuat tabungan"
                Log.e(TAG, "Failed to create saving: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating saving: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun addToSaving(id: String, amount: Double): Result<Saving> {
        return try {
            Log.d(TAG, "Adding to saving: id=$id, amount=$amount")

            val response = savingApi.addToSaving(id, mapOf("amount" to amount))

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val saving = Saving(
                        id = dto.id,
                        name = dto.name,
                        targetAmount = dto.targetAmount,
                        currentAmount = dto.currentAmount,
                        fillingPlan = when (dto.fillingPlan) {
                            "DAILY" -> FillingPlan.DAILY
                            "WEEKLY" -> FillingPlan.WEEKLY
                            "MONTHLY" -> FillingPlan.MONTHLY
                            else -> FillingPlan.MONTHLY
                        },
                        progress = dto.progress,
                        userId = dto.userId,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                    Result.success(saving)
                } else {
                    Result.failure(Exception("Data tabungan tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal menambah tabungan"
                Log.e(TAG, "Failed to add to saving: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception adding to saving: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun deleteSaving(id: String): Result<String> {
        return try {
            Log.d(TAG, "Deleting saving: id=$id")

            val response = savingApi.deleteSaving(id)

            if (response.isSuccessful && response.body()?.success == true) {
                val message = response.body()?.message ?: "Tabungan berhasil dihapus"
                Result.success(message)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal menghapus tabungan"
                Log.e(TAG, "Failed to delete saving: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception deleting saving: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }
}