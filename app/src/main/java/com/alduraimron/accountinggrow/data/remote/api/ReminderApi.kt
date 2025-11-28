package com.alduraimron.accountinggrow.data.remote.api

import com.alduraimron.accountinggrow.data.remote.dto.ApiResponse
import com.alduraimron.accountinggrow.data.remote.dto.ReminderDto
import com.alduraimron.accountinggrow.data.remote.dto.ReminderRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReminderApi {

    @GET("reminders")
    suspend fun getReminders(
        @Query("isPaid") isPaid: Boolean? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<ApiResponse<List<ReminderDto>>>

    @GET("reminders/upcoming")
    suspend fun getUpcomingReminders(): Response<ApiResponse<List<ReminderDto>>>

    @GET("reminders/{id}")
    suspend fun getReminderById(
        @Path("id") id: String
    ): Response<ApiResponse<ReminderDto>>

    @POST("reminders")
    suspend fun createReminder(
        @Body reminder: ReminderRequest
    ): Response<ApiResponse<ReminderDto>>

    @PUT("reminders/{id}")
    suspend fun updateReminder(
        @Path("id") id: String,
        @Body reminder: ReminderRequest
    ): Response<ApiResponse<ReminderDto>>

    @PATCH("reminders/{id}/mark-paid")
    suspend fun markReminderAsPaid(
        @Path("id") id: String
    ): Response<ApiResponse<ReminderDto>>

    @DELETE("reminders/{id}")
    suspend fun deleteReminder(
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>
}