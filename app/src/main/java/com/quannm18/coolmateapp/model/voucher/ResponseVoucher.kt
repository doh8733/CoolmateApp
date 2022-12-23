package com.quannm18.coolmateapp.model.voucher

import com.google.gson.annotations.SerializedName

data class ResponseVoucher(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("condition")
    val condition: String = "",
    @SerializedName("discount")
    val discount: Long = 0L,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("startDate")
    val startDate: String = "",
    @SerializedName("endDate")
    val endDate: String = "",
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("isMonopoly")
    val isMonopoly: Boolean = false,
    @SerializedName("value")
    val value: Int = 0,
    @SerializedName("used")
    val used: Long = 0L,

    @SerializedName("userId")
    val userID: String = ""
)