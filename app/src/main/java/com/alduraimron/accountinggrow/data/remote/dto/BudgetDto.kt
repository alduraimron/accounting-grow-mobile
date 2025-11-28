package com.alduraimron.accountinggrow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BudgetDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("budgetType")
    val budgetType: String,
    @SerializedName("month")
    val month: Int,
    @SerializedName("year")
    val year: Int,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("category")
    val category: CategoryDto?,
    @SerializedName("spent")
    val spent: Double,
    @SerializedName("remaining")
    val remaining: Double,
    @SerializedName("percentage")
    val percentage: Double,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)