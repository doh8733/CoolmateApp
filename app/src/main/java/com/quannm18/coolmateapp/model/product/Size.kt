package com.quannm18.coolmateapp.model.product

import com.google.gson.annotations.SerializedName

data class Size(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("productCount")
    val productCount: Int = 0,
    var isChecked : Boolean = false
)