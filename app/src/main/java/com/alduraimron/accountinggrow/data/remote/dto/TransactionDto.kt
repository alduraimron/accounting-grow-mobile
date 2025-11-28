package com.alduraimron.accountinggrow.data.remote.dto

import com.google.gson.annotations.SerializedName

// Transaction DTO
data class TransactionDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("nominal")
    val nominal: Double,
    @SerializedName("description")
    val description: String?,
    @SerializedName("date")
    val date: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("category")
    val category: CategoryDto?
)

// Transaction Summary DTO
data class TransactionSummaryDto(
    @SerializedName("totalIncome")
    val totalIncome: Double,
    @SerializedName("totalExpense")
    val totalExpense: Double,
    @SerializedName("balance")
    val balance: Double,
    @SerializedName("transactionCount")
    val transactionCount: Int
)

// Category DTO
data class CategoryDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?
)

// Transactions Response with Pagination
data class TransactionsResponseDto(
    @SerializedName("transactions")
    val transactions: List<TransactionDto>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)