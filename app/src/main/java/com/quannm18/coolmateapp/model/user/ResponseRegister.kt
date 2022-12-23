package com.quannm18.coolmateapp.model.user

data class ResponseRegister(
    val address: String? = "",
    val avatar: String? = "",
    val birthday: String? = "",
    val chatLink: String? = "",
    val createdAt: String? = "",
    val deletedAt: String? = "",
    val email: String? = "",
    val fullName: String? = "",
    val gender: String? = "",
    val id: String? = "",
    val isCreate: Boolean = false,
    val otp: String? = "",
    val password: String? = "",
    val phone: String? = "",
    val phoneActive: String? = "",
    val registrationToken: String? = "",
    val role: String? = "",
    val status: String? = "",
    val updatedAt: String? = ""
)