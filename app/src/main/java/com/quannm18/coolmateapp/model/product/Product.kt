package com.quannm18.coolmateapp.model.product

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("modelID")
    val modelID: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("productName")
    val productName: String = "",
    @SerializedName("image")
    val image: List<String> = emptyList(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("productCount")
    val productCount: Long = 0,
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("rebate")
    val rebate: Double = 0.0,
    @SerializedName("price")
    val price: Long = 0,
    @SerializedName("sellingPrice")
    val sellingPrice: Long = 0,
    @SerializedName("promotionalPrice")
    val promotionalPrice: Float = 0.0F,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("brand")
    val brand: String = "",
    @SerializedName("ratingAvg")
    val ratingAvg: Double = 0.0,
    @SerializedName("style")
    val style: String = "",
    @SerializedName("catalog")
    val catalog: String = "",
    @SerializedName("material")
    val material: String = "",
    @SerializedName("purpose")
    val purpose: List<String> = emptyList(),
    @SerializedName("feature")
    val feature: List<String>  = emptyList(),
    @SerializedName("color")
    val color: List<Color>  = emptyList()
)