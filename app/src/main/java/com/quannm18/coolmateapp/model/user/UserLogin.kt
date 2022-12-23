package com.quannm18.coolmateapp.model.user

import com.google.gson.annotations.SerializedName

data class UserLogin(
    var email: String = "",
    var password: String = "",
    var timeLogin : Long? = null,
    @SerializedName("statusCode")
    var statusCode : Int? = null,
    @SerializedName("message")
    var message : String? = null,
)