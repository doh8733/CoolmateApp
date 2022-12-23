package com.quannm18.coolmateapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.quannm18.coolmateapp.base.BaseViewModel
import com.quannm18.coolmateapp.bus.ListenerValidate
import com.quannm18.coolmateapp.model.user.ResponseLogin
import com.quannm18.coolmateapp.model.user.UserLogin
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.network.repository.UserRepositoryAPI
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import com.quannm18.coolmateapp.utils.Resource
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.utils.ValidateData.Companion.validateEmail
import com.quannm18.coolmateapp.view.activity.LoginActivity
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody

class LoginViewModel(application: Application, handle: SavedStateHandle) :
    BaseViewModel(
        application,
        handle
    ) {
    private val listener: MutableLiveData<Any> = MutableLiveData()
    val event: LiveData<Any> by lazy {
        listener
    }
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }

    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private val userRepositoryAPI: UserRepositoryAPI = UserRepositoryAPI.newInstance(application)

    fun loginAndValidate(userLogin: UserLogin) {
        userLogin.let {
            if (it.email.trim().isEmpty() || it.password.trim().isEmpty()) {
                listener.postValue(
                    ListenerValidate(
                        message = "Vui lòng nhập đầy đủ dữ liệu!"
                    )
                )
                return
            }
            if (!it.email.validateEmail() || it.email.substring(
                    0,
                    it.email.indexOf("@")
                ).length < 6
            ) {
                listener.postValue(
                    ListenerValidate(
                        message = "Email sai định dạng!"
                    )
                )
                return
            }
            if (it.password.trim().length < 6 || it.password.contains(" ")) {
                listener.postValue(
                    ListenerValidate(
                        message = "Mật khẩu hoặc tài khoản không chính xác!"
//                        message = "Mật khẩu tối thiểu 6 kí tự và không chứa khoảng trắng!"
                    )
                )
                return
            }
            listener.postValue(
                ListenerValidate(
                    message = "Kiểm tra dữ liệu thành công",
                    Status.SUCCESS
                )
            )
            return
        }
    }

    fun saveTokenAndRememberUserData(
        res: ResponseLogin,
        isChecked: Boolean = false,
        userLogin: UserLogin
    ) {
        if (isChecked) {
            managerSaveLocal.saveLogin(userLogin)
        } else {
            managerSaveLocal.deleteLogin()
        }
        Log.e(LoginActivity.TAG, "listeners: $res")
        if (res.token != null) {
            sessionManager.saveAuthToken(res.token.accessToken)
            sessionManager.saveUserInfo(res)
        }
    }

    suspend fun getUserListFromAPI() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepositoryAPI.getAllUserFromAPI()))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun getUserByIDFromAPI(id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepositoryAPI.getUserByID(id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun login(userLogin: UserLogin) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepositoryAPI.login(userLogin)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun me(authToken: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepositoryAPI.me(authToken)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun changeAvatar(authToken: String, media: MultipartBody.Part) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepositoryAPI.changeAvatar(authToken, media)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun infoEditUser(
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
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))

        try {
            emit(
                Resource.success(
                    data = userRepositoryAPI.editInfoUser(
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
                )
            )
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "ERROR"))
        }
    }

    fun putChatLink(
        token: String, chatLink: String
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = userRepositoryAPI.putChatLink(
                        token,
                        chatLink
                    )
                )
            )
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "ERROR"))
        }
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