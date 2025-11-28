package com.alduraimron.accountinggrow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReminderDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("dueDate")
    val dueDate: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("recurrence")
    val recurrence: String,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("isPaid")
    val isPaid: Boolean,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)