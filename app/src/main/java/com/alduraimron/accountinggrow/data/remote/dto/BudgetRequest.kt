package com.alduraimron.accountinggrow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BudgetRequest(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("budgetType")
    val budgetType: String,
    @SerializedName("month")
    val month: Int,
    @SerializedName("year")
    val year: Int,
    @SerializedName("notes")
    val notes: String? = null,
    @SerializedName("categoryId")
    val categoryId: String
)