package com.quannm18.coolmateapp.model.order

import com.google.gson.annotations.SerializedName
import com.quannm18.coolmateapp.model.cart.ProductXCart
import com.quannm18.coolmateapp.model.enum.MyEnum

data class ResponsePostOrder(
    @SerializedName("customerName")
    val customerName: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("paymentMethod")
    val paymentMethod: String ="",
    @SerializedName("placeStore")
    val placeStore: String ="",
    @SerializedName("placeCustomer")
    val placeCustomer: String ="",
    @SerializedName("idPayment")
    val idPayment: String = "",

    @SerializedName("cartID")
    val cartID: String ="",

    @SerializedName("voucherID")
    val voucherID: List<String?> = emptyList(),

    @SerializedName("cartProduct")
    val cartProduct: List<ProductXCart> = emptyList(),
    @SerializedName("shippingStatus")
    val shippingStatus: List<ShippingStatus> =  emptyList(),
    @SerializedName("note")
    val note: String = "",
    @SerializedName("vouchers")
    val vouchers: List<String?> = emptyList(),

    @SerializedName("userID")
    val userID: String = "",

    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("id")
    val id: String = ""
)


data class ShippingStatus(
    @SerializedName("shippingStatus")
    val shippingStatus: String= MyEnum.ShippingStatus.CHUATHANHTOAN,
    @SerializedName("note")
    val note: String = "",
    @SerializedName("createdAt")
    val createdAt: String = ""
)
