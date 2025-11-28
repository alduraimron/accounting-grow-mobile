package com.alduraimron.accountinggrow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TransactionRequest(
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("nominal")
    val nominal: Double,
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String? = null
)