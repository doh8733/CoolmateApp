package com.quannm18.coolmateapp.model.user

data class ChangePasswordResponse(
    val address: String,
    val avatar: String,
    val birthday: String,
    val chatLink: String,
    val createdAt: String,
    val deletedAt: Any,
    val email: String,
    val fullName: String,
    val gender: String,
    val id: String,
    val isCreate: Boolean,
    val otp: Any,
    val password: String,
    val phone: String,
    val phoneActive: String,
    val registrationToken: String,
    val role: String,
    val status: String,
    val updatedAt: String,
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String,
)