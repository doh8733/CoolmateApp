package com.quannm18.coolmateapp.network.auth

import android.content.Context
import android.content.SharedPreferences
import com.orhanobut.hawk.Hawk
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.user.ResponseLogin

class SessionManager () {
    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_INFO = "user_info"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        Hawk.put(USER_TOKEN, token)
    }

    fun saveUserInfo(responseLogin: ResponseLogin) {
        Hawk.put<ResponseLogin>(USER_INFO, responseLogin)
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return Hawk.get<String>(USER_TOKEN, null)
    }
    fun getUserInfo(): ResponseLogin {
        return Hawk.get<ResponseLogin>(USER_INFO, null)
    }
}