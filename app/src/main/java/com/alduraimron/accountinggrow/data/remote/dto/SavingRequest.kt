package com.alduraimron.accountinggrow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SavingRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("targetAmount")
    val targetAmount: Double,
    @SerializedName("currentAmount")
    val currentAmount: Double,
    @SerializedName("fillingPlan")
    val fillingPlan: String
)

data class AddToSavingRequest(
    @SerializedName("amount")
    val amount: Double
)