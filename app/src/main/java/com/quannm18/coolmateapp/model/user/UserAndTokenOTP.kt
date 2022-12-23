package com.quannm18.coolmateapp.model.user

data class UserAndTokenOTP(
    val token: Token =  Token(),
    val userInfo :  UserInfo = UserInfo()
)