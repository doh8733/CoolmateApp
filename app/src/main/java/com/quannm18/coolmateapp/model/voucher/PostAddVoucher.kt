package com.quannm18.coolmateapp.model.voucher

import com.google.gson.annotations.SerializedName

data class PostAddVoucher(
    @SerializedName("voucherId")
    private val voucherId: String = ""
)