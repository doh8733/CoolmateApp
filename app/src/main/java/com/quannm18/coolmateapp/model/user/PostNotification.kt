package com.quannm18.coolmateapp.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostNotification(
    @SerializedName("registrationToken")
    var registrationToken: String = ""
)