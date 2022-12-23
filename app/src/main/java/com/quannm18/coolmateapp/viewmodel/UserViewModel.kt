package com.quannm18.coolmateapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.quannm18.coolmateapp.base.BaseViewModel
import com.quannm18.coolmateapp.database.repository.UserDBRepository
import com.quannm18.coolmateapp.model.user.PostNotification
import com.quannm18.coolmateapp.model.user.UserInfo
import com.quannm18.coolmateapp.network.api.ApiConfig
import com.quannm18.coolmateapp.network.api.ApiConfigWithAuth
import com.quannm18.coolmateapp.network.repository.UserRepositoryAPI
import com.quannm18.coolmateapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application, handle: SavedStateHandle) : BaseViewModel(
    application,
    handle
) {
    private val userDBRepository: UserDBRepository = UserDBRepository.newInstance(application)
    private val userRepositoryAPI: UserRepositoryAPI = UserRepositoryAPI.newInstance(application)

    private val listenerToken : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val eventToken: LiveData<String> by lazy {
        listenerToken
    }

    fun insertUser(userInfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            userDBRepository.insertUser(userInfo)
        }
    }

    fun insertUser(userInfoList: List<UserInfo>) {
        viewModelScope.launch(Dispatchers.IO) {
            userDBRepository.insertUser(userInfoList)
        }
    }

    fun getUserListFromDB(): LiveData<List<UserInfo>> {
        return userDBRepository.selectAll()
    }

    fun getUserByIDFromDB(id: String): LiveData<UserInfo> {
        return userDBRepository.getUserByID(id)
    }
    fun getOtpFromEmail(email: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = ApiConfig.apiService.getOtpFromEmail(email)))
        }catch (e : Exception){
            emit(Resource.error(data = null,message = e.message?: "ERROR"))
        }
    }
    fun postResetPassword(email: String,otp :String,newPassword: String,confirmPassword : String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = ApiConfig.apiService.postResetPassword(email,otp, newPassword, confirmPassword)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message?: "ERROR"))
        }
    }
    fun putTokenNotification(token: String, notification: PostNotification) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = ApiConfigWithAuth.apiService.putTokenNotification(token, notification)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message?: "ERROR"))
        }
    }

    fun putAddress(token: String, userInfo: UserInfo) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = ApiConfigWithAuth.apiService.putAddress(token, userInfo)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message?: "ERROR"))
        }
    }

    fun postTokenNotification(token: String?) {
        listenerToken.postValue(token)
    }
    fun putAddress(
        token: String, address: String
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))

        try {
            emit(
                Resource.success(
                    data = userRepositoryAPI.putAddress(
                        token,
                        address
                    )
                )
            )
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "ERROR"))
        }
    }
}