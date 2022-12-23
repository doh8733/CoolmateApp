package com.quannm18.coolmateapp.model.user

import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("user")
    val user: UserInfo? = null,
    @SerializedName("token")
    val token: Token? = null
)