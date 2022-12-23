package com.quannm18.coolmateapp.model.favorite

import com.google.gson.annotations.SerializedName
import com.quannm18.coolmateapp.model.product.Product

data class ResponseFavorite(
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("deletedAt")
    val deletedAt: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("product")
    val product: Product = Product(),
    @SerializedName("productId")
    val productId: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("userId")
    val userId: String = ""
)