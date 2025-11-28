package com.alduraimron.accountinggrow.domain.model

data class Transaction(
    val id: String,
    val userId: String,
    val categoryId: String,
    val type: TransactionType,
    val nominal: Double,
    val description: String?,
    val date: String,
    val createdAt: String,
    val category: Category?
)

enum class TransactionType {
    INCOME,
    EXPENSE
}

data class TransactionSummary(
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double,
    val transactionCount: Int
)

data class Category(
    val id: String,
    val name: String,
    val type: TransactionType
)

data class Saving(
    val id: String,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val fillingPlan: FillingPlan,
    val progress: Double,
    val userId: String,
    val createdAt: String,
    val updatedAt: String
)

enum class FillingPlan {
    DAILY,
    WEEKLY,
    MONTHLY
}

data class Budget(
    val id: String,
    val amount: Double,
    val budgetType: BudgetType,
    val month: Int,
    val year: Int,
    val notes: String?,
    val categoryId: String,
    val userId: String,
    val category: Category?,
    val spent: Double,
    val remaining: Double,
    val percentage: Double,
    val createdAt: String,
    val updatedAt: String
)

enum class BudgetType {
    DAILY,
    WEEKLY,
    MONTHLY
}

data class Reminder(
    val id: String,
    val name: String,
    val dueDate: String,
    val amount: Double,
    val recurrence: Recurrence,
    val notes: String?,
    val isPaid: Boolean,
    val userId: String,
    val createdAt: String,
    val updatedAt: String
)

enum class Recurrence {
    NONE,
    DAILY,
    WEEKLY,
    MONTHLY
}

data class Article(
    val id: String,
    val title: String,
    val content: String,
    val publishedDate: String,
    val createdAt: String,
    val updatedAt: String
)

data class UserProfile(
    val id: String,
    val username: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val bio: String?,
    val createdAt: String,
    val updatedAt: String
)