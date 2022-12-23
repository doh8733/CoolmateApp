package com.quannm18.coolmateapp.model.order

import com.google.gson.annotations.SerializedName
import com.quannm18.coolmateapp.model.cart.ProductXCart
import com.quannm18.coolmateapp.model.enum.MyEnum

data class OrderPost(
    @SerializedName("customerName")
    var customerName: String = "",
    @SerializedName("status")
    var status: String = MyEnum.ShippingStatus.CHUATHANHTOAN,
    @SerializedName("paymentMethod")
    var paymentMethod: String = "",
    @SerializedName("placeStore")
    val placeStore: String = "Toà nhà BMM, KM2 Đ. Phùng Hưng, Hà Đông, Hà Nội",
    @SerializedName("placeCustomer")
    var placeCustomer: String = "",
    @SerializedName("cartId")
    var cartID: String = "",
    @SerializedName("cartProduct")
    var cartProduct: MutableList<ProductXCart> = mutableListOf<ProductXCart>(),
    @SerializedName("idPayment")
    var idPayment: String = "",

    @SerializedName("voucherId")
    val voucherID: List<String> = emptyList(),

    @SerializedName("note")
    val note: String = "",
)
