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
import com.quannm18.coolmateapp.model.voucher.PostAddVoucher
import com.quannm18.coolmateapp.network.api.ApiConfig
import com.quannm18.coolmateapp.network.api.ApiConfigWithAuth
import com.quannm18.coolmateapp.network.repository.VoucherRepositoryAPI
import com.quannm18.coolmateapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoucherViewModel(application: Application, handle: SavedStateHandle) : BaseViewModel(
    application,
    handle
) {
    private val voucherRepositoryAPI = VoucherRepositoryAPI.newInstance(application)

    private val listenerToken : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val eventToken: LiveData<String> by lazy {
        listenerToken
    }

    fun getVoucherByUserId(token: String, id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = ApiConfigWithAuth.apiService.getVoucherByUserId(token, id)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message?: "ERROR"))
        }
    }

    fun addToWalletVoucher(token: String, postAddVoucher: PostAddVoucher) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = ApiConfigWithAuth.apiService.addToWalletVoucher(token, postAddVoucher)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message?: "ERROR"))
        }
    }
}