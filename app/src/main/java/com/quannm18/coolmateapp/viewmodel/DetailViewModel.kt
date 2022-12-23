package com.quannm18.coolmateapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.quannm18.coolmateapp.base.BaseViewModel
import com.quannm18.coolmateapp.model.cart.AddCart
import com.quannm18.coolmateapp.model.product.Product
import com.quannm18.coolmateapp.network.repository.CartRepositoryAPI
import com.quannm18.coolmateapp.network.repository.ProductRepositoryAPI
import com.quannm18.coolmateapp.utils.Resource
import com.quannm18.coolmateapp.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application,
                      handle: SavedStateHandle
) : BaseViewModel(application, handle) {
    private val listProductDetail : MutableLiveData<Product> by lazy {
        MutableLiveData()
    }

    val event : LiveData<Product> by lazy {
        listProductDetail
    }
    private val productRepositoryAPI: ProductRepositoryAPI =
        ProductRepositoryAPI.newInstance(application)
    private val cartRepositoryAPI: CartRepositoryAPI = CartRepositoryAPI.newInstance(application)

    fun getProductByID(authToken: String, id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(productRepositoryAPI.getProductByID(authToken, id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }


    fun addToCart(authToken: String, addCart: AddCart) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepositoryAPI.addToCart(authToken, addCart)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }
    fun setProduct(product: Product) {
        viewModelScope.launch {
            listProductDetail.value= (product)
        }
    }
}