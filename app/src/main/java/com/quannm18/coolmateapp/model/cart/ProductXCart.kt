package com.quannm18.coolmateapp.model.cart

import com.google.gson.annotations.SerializedName

data class ProductXCart(
    @SerializedName("colorName")
    var colorName: String = "",
    @SerializedName("productId")
    var productId: String = "",
    @SerializedName("quantity")
    var quantity: Int = 0,
    @SerializedName("sizeName")
    var sizeName: String = "",

    var itemCartID: String = ""
)