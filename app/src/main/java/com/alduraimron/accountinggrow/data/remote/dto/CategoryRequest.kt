package com.alduraimron.accountinggrow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoryRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)