package com.quannm18.coolmateapp.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class PutChangeAddress(
    @SerializedName("address")
    var address: String = "",
)