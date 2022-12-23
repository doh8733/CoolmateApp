package com.quannm18.coolmateapp.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quannm18.coolmateapp.model.cart.CartProduct

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(products: CartProduct)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productsList: List<CartProduct>)

    @Query("SELECT * FROM cart")
    fun selectAll(): LiveData<List<CartProduct>>

    @Query("SELECT sum(total_price) FROM cart where is_checked = 1")
    fun selectSum(): LiveData<Long>

    @Query("SELECT * FROM cart")
    fun pagingAll(): PagingSource<Int, CartProduct>

    @Query("SELECT * FROM cart where is_checked = 1")
    fun pagingChecked(): PagingSource<Int, CartProduct>

    @Query("update cart set is_checked  = :isChecked where product_id =:productId")
    fun setChecked(isChecked: Boolean, productId: String)

    @Query("Select * from cart where is_checked = 1")
    fun selectAllChecked(): LiveData<List<CartProduct>>

    @Query("Select sum(total_price) from cart where is_checked = 1")
    fun totalPrice(): LiveData<Long>

    @Query("delete  from cart")
    fun delete()

    @Query("delete from cart where item_id = :itemID")
    fun delete(itemID: String)

    @Query("delete from cart where is_checked = 1")
    fun deleteChecked()

}