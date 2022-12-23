package com.quannm18.coolmateapp.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Query
import com.quannm18.coolmateapp.database.room.RoomDB
import com.quannm18.coolmateapp.model.cart.CartProduct
import com.quannm18.coolmateapp.model.user.UserInfo

class CartDBRepository(application: Application) {
    companion object {
        private var instance: CartDBRepository? = null
        fun newInstance(application: Application): CartDBRepository {
            if (instance == null) {
                instance = CartDBRepository(application)
            }
            return instance!!
        }
    }

    private val cartDao = RoomDB.getInstance(application).cartDao()

    fun insert(cartProduct: CartProduct) {
        cartDao.insert(cartProduct)
    }

    fun insert(cartList : List<CartProduct>) {
        cartDao.insert(cartList)
    }
    fun deleteChecked() {
        cartDao.deleteChecked()
    }

    fun selectAllChecked(): LiveData<List<CartProduct>> {
        return cartDao.selectAllChecked()
    }

    fun totalPrice(): LiveData<Long> {
        return cartDao.totalPrice()
    }

    fun delete(itemID: String? = null) {
        if (itemID==null){
            cartDao.delete()
        }else{
            cartDao.delete(itemID)
        }
    }

    fun selectAll(): LiveData<List<CartProduct>> {
        return cartDao.selectAll()
    }
    fun selectSum(): LiveData<Long> {
        return cartDao.selectSum()
    }
    fun pagingAll(): PagingSource<Int, CartProduct> {
        return cartDao.pagingAll()
    }

    fun pagingChecked(): PagingSource<Int, CartProduct> {
        return cartDao.pagingChecked()
    }

    fun setChecked(isChecked: Boolean, productId: String) {
        cartDao.setChecked(isChecked,productId)
    }

}