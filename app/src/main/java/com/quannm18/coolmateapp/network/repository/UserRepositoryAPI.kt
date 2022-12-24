package com.quannm18.coolmateapp.network.repository

import android.app.Application
import androidx.lifecycle.liveData
import com.quannm18.coolmateapp.model.user.UserLogin
import com.quannm18.coolmateapp.network.api.ApiConfig
import com.quannm18.coolmateapp.network.api.ApiConfigWithAuth
import okhttp3.MultipartBody
import retrofit2.http.Part

class UserRepositoryAPI(var application: Application) {
    companion object {
        private var instance: UserRepositoryAPI? = null
        fun newInstance(application: Application): UserRepositoryAPI {
            if (instance == null) {
                instance = UserRepositoryAPI(application)
            }
            return instance!!
        }
    }

    suspend fun editInfoUser(
        token: String,
        fullName: String,
        gender: String,
        birthday: String,
        address: String,
        phoneNumber: String,
        chatLink: String,
        avatar: String,
        password: String,
        phoneActive: String
    ) = ApiConfigWithAuth.apiService.editInfoUser(
        token,
        fullName,
        gender,
        birthday,
        address,
        phoneNumber,
        chatLink,
        avatar,
        password,
        phoneActive
    )

    suspend fun putAddress(token: String, address: String) =
        ApiConfigWithAuth.apiService.putAddress(token, address)

    suspend fun putChatLink(token: String, chatLink: String) =
        ApiConfigWithAuth.apiService.putChatLink(token, chatLink)

    suspend fun getAllUserFromAPI() = ApiConfig.apiService.getListUser()

    suspend fun getUserByID(id: String) = ApiConfig.apiService.getUserByID(id)
    suspend fun login(userLogin: UserLogin) = ApiConfig.apiService.login(userLogin)
    suspend fun me(authToken: String) = ApiConfigWithAuth.apiService.me(authToken)
    suspend fun changeAvatar(authToken: String, @Part media: MultipartBody.Part) =
        ApiConfigWithAuth.apiService.changeAvatar(authToken, media)

    suspend fun changePassword(
        token: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) = ApiConfig.apiService.changePassword(
        token,
        currentPassword,
        newPassword,
        confirmPassword
    )

}