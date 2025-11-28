package com.alduraimron.accountinggrow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReminderRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("dueDate")
    val dueDate: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("recurrence")
    val recurrence: String,
    @SerializedName("notes")
    val notes: String? = null
)