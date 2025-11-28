package com.alduraimron.accountinggrow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("bio")
    val bio: String? = null
)

data class ChangePasswordRequest(
    @SerializedName("currentPassword")
    val currentPassword: String,
    @SerializedName("newPassword")
    val newPassword: String
)