package com.quannm18.coolmateapp.model.user

data class User(
    val address: String,
    val birthday: String,
    val createdAt: String,
    val deletedAt: Any,
    val email: String,
    val fullName: String,
    val gender: String,
    val id: String,
    val isCreate: Boolean,
    val otp: Any,
    val password: String,
    val role: String,
    val status: String,
    val updatedAt: String
)