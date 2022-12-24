package com.quannm18.coolmateapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.quannm18.coolmateapp.base.BaseViewModel
import com.quannm18.coolmateapp.database.repository.CartDBRepository
import com.quannm18.coolmateapp.model.cart.CartProduct
import com.quannm18.coolmateapp.model.itemcart.PutItemCart
import com.quannm18.coolmateapp.model.itemcart.ResponseItemCart
import com.quannm18.coolmateapp.model.order.OrderPost
import com.quannm18.coolmateapp.model.order.ShippingStatus
import com.quannm18.coolmateapp.network.repository.CartRepositoryAPI
import com.quannm18.coolmateapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CartViewModel(application: Application, handle: SavedStateHandle) : BaseViewModel(
    application,
    handle
) {
    private val listenerEvent: MutableLiveData<Any> = MutableLiveData()
    val event: LiveData<Any> by lazy {
        listenerEvent
    }

    private val cartRepositoryAPI: CartRepositoryAPI = CartRepositoryAPI.newInstance(application)
    private val cartDBRepository: CartDBRepository = CartDBRepository.newInstance(application)

    val listCartLiveData: LiveData<List<CartProduct>> = cartDBRepository.selectAll()
    fun selectAllChecked(): LiveData<List<CartProduct>> {
        return cartDBRepository.selectAllChecked()
    }

    val selectSum: LiveData<Long> = cartDBRepository.selectSum()
    val totalPrice: LiveData<Long> = cartDBRepository.totalPrice()

    fun getCart(authToken: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepositoryAPI.getCart(authToken)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun deleteCart(authToken: String, cartID: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepositoryAPI.deleteCart(authToken, cartID)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun postOrders(authToken: String, orderPost: OrderPost) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepositoryAPI.postOrders(authToken, orderPost)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun getInfoBillByID(authToken: String, idBill: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepositoryAPI.getInfoBillByID(authToken, idBill)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun getAllItemCart(authToken: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepositoryAPI.getAllItemCart(authToken)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun updateItemCart(authToken: String, putItemCart: PutItemCart, idItemCart: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepositoryAPI.updateItemCart(authToken, putItemCart, idItemCart)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun getCartByID(authToken: String, id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(cartRepositoryAPI.getCartByID(authToken, id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }
    fun huyDonHang(token :String ,id: String,shippingStatus: String,note :String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(data = cartRepositoryAPI.huyDon(token,id,shippingStatus, note)))
        }catch (e :Exception){
            emit(Resource.error(data = null,e.message?: ""))
        }
    }

    val pagingAll: Flow<PagingData<CartProduct>> by lazy {
        Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            )
        ) {
            cartDBRepository.pagingAll()
        }.flow.cachedIn(viewModelScope)
    }

    val pagingChecked: Flow<PagingData<CartProduct>> by lazy {
        Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            )
        ) {
            cartDBRepository.pagingChecked()
        }.flow.cachedIn(viewModelScope)
    }

    fun insert(cart: CartProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDBRepository.insert(cart)
        }
    }

    fun delete(itemID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDBRepository.delete(itemID)
        }
    }

    fun deleteChecked() {
        viewModelScope.launch(Dispatchers.IO) {
            cartDBRepository.deleteChecked()
        }
    }

    fun setSelected(isChecked: Boolean, productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDBRepository.setChecked(isChecked, productId)
        }
    }

    fun deleteCartItem(orderPost: OrderPost, token: String) {
        viewModelScope.launch {
            orderPost.cartProduct.map {
                deleteCart(token, cartID = it.itemCartID)
                deleteChecked()
            }

        }
    }

}