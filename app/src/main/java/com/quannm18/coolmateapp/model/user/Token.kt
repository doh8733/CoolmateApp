package com.quannm18.coolmateapp.model.user

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("expiresIn")
    val expiresIn: Int = 0,
    @SerializedName("accessToken")
    val accessToken: String = ""
)