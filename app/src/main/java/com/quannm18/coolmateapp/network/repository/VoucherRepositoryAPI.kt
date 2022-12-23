package com.quannm18.coolmateapp.network.repository

import android.app.Application
import com.quannm18.coolmateapp.model.cart.AddCart
import com.quannm18.coolmateapp.model.itemcart.PutItemCart
import com.quannm18.coolmateapp.model.itemcart.ResponseItemCart
import com.quannm18.coolmateapp.model.order.OrderPost
import com.quannm18.coolmateapp.network.api.ApiConfigWithAuth
import retrofit2.http.Path

class VoucherRepositoryAPI(var application: Application) {
    companion object {
        private var instance: VoucherRepositoryAPI? = null
        fun newInstance(application: Application): VoucherRepositoryAPI {
            if (instance == null) {
                instance = VoucherRepositoryAPI(application)
            }
            return instance!!
        }
    }
    suspend fun updateItemCart(authToken: String, userId: String) = ApiConfigWithAuth.apiService.getVoucherByUserId(authToken, userId)
}