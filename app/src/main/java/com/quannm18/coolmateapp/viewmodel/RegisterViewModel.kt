package com.quannm18.coolmateapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.quannm18.coolmateapp.base.BaseViewModel
import com.quannm18.coolmateapp.bus.ListenerValidate
import com.quannm18.coolmateapp.model.user.UserRegister
import com.quannm18.coolmateapp.network.api.ApiConfig
import com.quannm18.coolmateapp.network.repository.UserRepositoryAPI
import com.quannm18.coolmateapp.utils.Resource
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.utils.ValidateData.Companion.isValidEmail
import com.quannm18.coolmateapp.utils.ValidateData.Companion.validateEmail
import com.quannm18.coolmateapp.utils.ValidateData.Companion.validateName
import kotlinx.coroutines.Dispatchers

class RegisterViewModel(
    application: Application,
    handle: SavedStateHandle
) : BaseViewModel(application, handle) {
    private val listener: MutableLiveData<Any> = MutableLiveData()
    val event: LiveData<Any> by lazy {
        listener
    }

    private val userRepositoryAPI: UserRepositoryAPI = UserRepositoryAPI.newInstance(application)

    fun registerAndValidate(userRegister: UserRegister, retypePassword: String) {
        userRegister.let {
            if (it.fullName.trim().isEmpty() || it.email.trim().isEmpty() || it.password.trim()
                    .isEmpty() || retypePassword.trim().isEmpty()
            ) {
                listener.postValue(
                    ListenerValidate(
                        message = "Vui lòng đầy đủ trường dữ liệu!"
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

            if (!it.fullName.validateName()) {
                listener.postValue(
                    ListenerValidate(
                        message = "Họ và tên sai định dạng!"
                    )
                )
                return
            }
            if (it.password.trim().length < 6 || it.password.contains(" ")) {
                listener.postValue(
                    ListenerValidate(
                        message = "Mật khẩu tối thiểu 6 kí tự và không chứa khoảng trắng!"
                    )
                )
                return
            }

            if (!retypePassword.trim().equals(it.password.trim())) {
                listener.postValue(
                    ListenerValidate(
                        message = "Mật khẩu không khớp"
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

    fun register(userRegister: UserRegister) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = ApiConfig.apiService.register(
                        fullName = userRegister.fullName,
                        email = userRegister.email,
                        password = userRegister.password,
                        gender = userRegister.gender,
                        birthday = userRegister.birthDay,
                        address = userRegister.address
                    )
                )
            )
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "ERR"))
        }
    }
}