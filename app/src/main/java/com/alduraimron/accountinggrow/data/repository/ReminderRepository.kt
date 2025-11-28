package com.alduraimron.accountinggrow.data.repository

import android.util.Log
import com.alduraimron.accountinggrow.data.remote.api.ReminderApi
import com.alduraimron.accountinggrow.domain.model.Recurrence
import com.alduraimron.accountinggrow.domain.model.Reminder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val reminderApi: ReminderApi
) {
    companion object {
        private const val TAG = "ReminderRepository"
    }

    suspend fun getReminders(isPaid: Boolean? = null): Result<List<Reminder>> {
        return try {
            Log.d(TAG, "Fetching reminders: isPaid=$isPaid")
            val response = reminderApi.getReminders(isPaid = isPaid)

            if (response.isSuccessful && response.body()?.success == true) {
                val reminderDtos = response.body()?.data ?: emptyList()
                val reminders = reminderDtos.map { dto ->
                    Reminder(
                        id = dto.id,
                        name = dto.name,
                        dueDate = dto.dueDate,
                        amount = dto.amount,
                        recurrence = when (dto.recurrence) {
                            "NONE" -> Recurrence.NONE
                            "DAILY" -> Recurrence.DAILY
                            "WEEKLY" -> Recurrence.WEEKLY
                            "MONTHLY" -> Recurrence.MONTHLY
                            else -> Recurrence.NONE
                        },
                        notes = dto.notes,
                        isPaid = dto.isPaid,
                        userId = dto.userId,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                }
                Log.d(TAG, "Fetched ${reminders.size} reminders")
                Result.success(reminders)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal mengambil pengingat"
                Log.e(TAG, "Failed to fetch reminders: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching reminders: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun getUpcomingReminders(): Result<List<Reminder>> {
        return try {
            Log.d(TAG, "Fetching upcoming reminders")
            val response = reminderApi.getUpcomingReminders()

            if (response.isSuccessful && response.body()?.success == true) {
                val reminderDtos = response.body()?.data ?: emptyList()
                val reminders = reminderDtos.map { dto ->
                    Reminder(
                        id = dto.id,
                        name = dto.name,
                        dueDate = dto.dueDate,
                        amount = dto.amount,
                        recurrence = when (dto.recurrence) {
                            "NONE" -> Recurrence.NONE
                            "DAILY" -> Recurrence.DAILY
                            "WEEKLY" -> Recurrence.WEEKLY
                            "MONTHLY" -> Recurrence.MONTHLY
                            else -> Recurrence.NONE
                        },
                        notes = dto.notes,
                        isPaid = dto.isPaid,
                        userId = dto.userId,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                }
                Log.d(TAG, "Fetched ${reminders.size} upcoming reminders")
                Result.success(reminders)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal mengambil pengingat"
                Log.e(TAG, "Failed to fetch upcoming reminders: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching upcoming reminders: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun createReminder(
        name: String,
        dueDate: String,
        amount: Double,
        recurrence: String,
        notes: String?
    ): Result<Reminder> {
        return try {
            Log.d(TAG, "Creating reminder: name=$name")

            val requestBody = buildMap<String, Any> {
                put("name", name)
                put("dueDate", dueDate)
                put("amount", amount)
                put("recurrence", recurrence)
                notes?.let { put("notes", it) }
            }

            val response = reminderApi.createReminder(requestBody)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val reminder = Reminder(
                        id = dto.id,
                        name = dto.name,
                        dueDate = dto.dueDate,
                        amount = dto.amount,
                        recurrence = when (dto.recurrence) {
                            "NONE" -> Recurrence.NONE
                            "DAILY" -> Recurrence.DAILY
                            "WEEKLY" -> Recurrence.WEEKLY
                            "MONTHLY" -> Recurrence.MONTHLY
                            else -> Recurrence.NONE
                        },
                        notes = dto.notes,
                        isPaid = dto.isPaid,
                        userId = dto.userId,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                    Log.d(TAG, "Reminder created successfully")
                    Result.success(reminder)
                } else {
                    Result.failure(Exception("Data pengingat tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal membuat pengingat"
                Log.e(TAG, "Failed to create reminder: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating reminder: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun markReminderAsPaid(id: String): Result<Reminder> {
        return try {
            Log.d(TAG, "Marking reminder as paid: id=$id")

            val response = reminderApi.markReminderAsPaid(id)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val reminder = Reminder(
                        id = dto.id,
                        name = dto.name,
                        dueDate = dto.dueDate,
                        amount = dto.amount,
                        recurrence = when (dto.recurrence) {
                            "NONE" -> Recurrence.NONE
                            "DAILY" -> Recurrence.DAILY
                            "WEEKLY" -> Recurrence.WEEKLY
                            "MONTHLY" -> Recurrence.MONTHLY
                            else -> Recurrence.NONE
                        },
                        notes = dto.notes,
                        isPaid = dto.isPaid,
                        userId = dto.userId,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                    Result.success(reminder)
                } else {
                    Result.failure(Exception("Data pengingat tidak valid"))
                }
            } else {
                val errorMessage = response.body()?.message ?: "Gagal menandai sebagai lunas"
                Log.e(TAG, "Failed to mark reminder as paid: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception marking reminder as paid: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }

    suspend fun deleteReminder(id: String): Result<String> {
        return try {
            Log.d(TAG, "Deleting reminder: id=$id")

            val response = reminderApi.deleteReminder(id)

            if (response.isSuccessful && response.body()?.success == true) {
                val message = response.body()?.message ?: "Pengingat berhasil dihapus"
                Result.success(message)
            } else {
                val errorMessage = response.body()?.message ?: "Gagal menghapus pengingat"
                Log.e(TAG, "Failed to delete reminder: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception deleting reminder: ${e.message}", e)
            Result.failure(Exception("Tidak dapat terhubung ke server: ${e.message}"))
        }
    }
}