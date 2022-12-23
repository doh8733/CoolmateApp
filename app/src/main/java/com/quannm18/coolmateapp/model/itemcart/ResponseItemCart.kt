package com.quannm18.coolmateapp.model.itemcart

import com.google.gson.annotations.SerializedName
import com.quannm18.coolmateapp.model.product.Product


data class ResponseItemCart(
    @SerializedName("id")
    val id: String ="",
    @SerializedName("name")
    val name: String ="",
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("products")
    val products: Products = Products(),

    @SerializedName("userId")
    val userID: String = ""
)

data class Products (
    @SerializedName("productId")
    val productID: String = "",

    @SerializedName("colorName")
    val colorName: String = "",
    @SerializedName("sizeName")
    val sizeName: String = "",
    @SerializedName("quantity")
    val quantity: Long = 0L,
    @SerializedName("product")
    val product: Product = Product()
)
