package com.quannm18.coolmateapp.model

data class ErrorRequest(
    val message: String?,
    val error: String?,
    val statusCode: Int?
)