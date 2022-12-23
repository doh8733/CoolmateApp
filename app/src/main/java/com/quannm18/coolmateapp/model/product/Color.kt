package com.quannm18.coolmateapp.model.product

import com.google.gson.annotations.SerializedName

data class Color(
    @SerializedName("name")
    val name: String?,
    @SerializedName("colorCode")
    val colorCode: String?,
    @SerializedName("image")
    val image: List<String>?,
    @SerializedName("size")
    val size: List<Size>?,
    var isChecked: Boolean = false
)