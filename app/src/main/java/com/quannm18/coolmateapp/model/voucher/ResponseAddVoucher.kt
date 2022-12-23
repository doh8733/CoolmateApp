package com.quannm18.coolmateapp.model.voucher

data class ResponseAddVoucher(
    val createdAt: String,
    val deletedAt: String,
    val id: String,
    val updatedAt: String,
    val userId: String,
    val voucher: String,
    val voucherId: String
)