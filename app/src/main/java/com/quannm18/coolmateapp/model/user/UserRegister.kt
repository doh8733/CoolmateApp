package com.quannm18.coolmateapp.model.user

data class UserRegister(
    var fullName: String = "",
    var email: String = "",
    var password: String = "",
    var gender: String = "NAM",
    var birthDay: String = "2022-12-04T22:43:41.536Z",
    var address: String = "HN",
)