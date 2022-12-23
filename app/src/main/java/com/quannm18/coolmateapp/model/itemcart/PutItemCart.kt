package com.quannm18.coolmateapp.model.itemcart

import com.google.gson.annotations.SerializedName

data class PutItemCart(
    @SerializedName("colorName")
    val colorName: String = "",
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SerializedName("sizeName")
    val sizeName: String ="",
    val itemCartId: String = ""
)