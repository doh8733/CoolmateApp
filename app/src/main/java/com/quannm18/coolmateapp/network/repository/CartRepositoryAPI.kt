package com.quannm18.coolmateapp.network.repository

import android.app.Application
import com.quannm18.coolmateapp.model.cart.AddCart
import com.quannm18.coolmateapp.model.itemcart.PutItemCart
import com.quannm18.coolmateapp.model.itemcart.ResponseItemCart
import com.quannm18.coolmateapp.model.order.OrderPost
import com.quannm18.coolmateapp.network.api.ApiConfig
import com.quannm18.coolmateapp.network.api.ApiConfigWithAuth

class CartRepositoryAPI(var application: Application) {
    companion object {
        private var instance: CartRepositoryAPI? = null
        fun newInstance(application: Application): CartRepositoryAPI {
            if (instance == null) {
                instance = CartRepositoryAPI(application)
            }
            return instance!!
        }
    }

    suspend fun addToCart(authToken: String, addCart: AddCart) = ApiConfigWithAuth.apiService.addToCart(authToken, addCart)
    suspend fun getCartByID(authToken: String, id: String) = ApiConfigWithAuth.apiService.getCartByID(authToken,id)
    suspend fun getCart(authToken: String) = ApiConfigWithAuth.apiService.getCart(authToken)
    suspend fun deleteCart(authToken: String, cartId: String) = ApiConfigWithAuth.apiService.deleteCart(authToken, cartId)
    suspend fun postOrders(authToken: String, orderPost: OrderPost) = ApiConfigWithAuth.apiService.postOrders(authToken, orderPost)
    suspend fun getInfoBillByID(authToken: String, idBill:String) = ApiConfigWithAuth.apiService.getInfoBillByID(authToken, idBill)

    suspend fun getAllItemCart(authToken: String) = ApiConfigWithAuth.apiService.getAllItemCart(authToken)
    suspend fun updateItemCart(authToken: String, itemCart: PutItemCart, idItemCart: String) = ApiConfigWithAuth.apiService.updateItemCart(authToken, itemCart, idItemCart)

    suspend fun huyDon(token :String,id:String, shippingStatus :String, note:String) = ApiConfig.apiService.huyDonhang(token,id,shippingStatus,note)

}