package com.quannm18.coolmateapp.model.filter

import com.google.gson.annotations.SerializedName

data class GetFilter(
    @SerializedName("authToken")
    var authToken: String? = "",
    @SerializedName("priceTo")
    var priceTo: Int? = 0,
    @SerializedName("productName")
    var productName: String? = "",
    @SerializedName("priceFrom")
    var priceFrom: Int? = 0,
    @SerializedName("style")
    var style: String? = "",
    @SerializedName("catalog")
    var catalog: String? = "",
    @SerializedName("material")
    var material: String? = "",
    @SerializedName("purpose")
    var purpose: String? = "",
    @SerializedName("feature")
    var feature: String? = ""
)