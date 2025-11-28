package com.alduraimron.accountinggrow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SavingDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("targetAmount")
    val targetAmount: Double,
    @SerializedName("currentAmount")
    val currentAmount: Double,
    @SerializedName("fillingPlan")
    val fillingPlan: String,
    @SerializedName("progress")
    val progress: Double,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)