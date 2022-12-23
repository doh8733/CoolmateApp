package com.quannm18.coolmateapp.model.cart

import com.google.gson.annotations.SerializedName

data class AddCart(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("products")
    var products: List<ProductXCart> = emptyList()
)