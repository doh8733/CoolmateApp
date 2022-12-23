package com.quannm18.coolmateapp.utils

import android.util.Log
import com.google.gson.Gson
import com.quannm18.coolmateapp.model.cart.CartProduct
import com.quannm18.coolmateapp.model.cart.ProductXCart
import com.quannm18.coolmateapp.model.order.OrderPost
import com.quannm18.coolmateapp.view.activity.LocationActivity.Companion.TAG

class ConvertData {
    companion object {
        fun convertCartToOrder(cartList: List<CartProduct>): OrderPost {
            val orderPost = OrderPost()
            cartList.map { cartProduct ->
                orderPost.cartProduct.add(
                    ProductXCart(
                        productId = cartProduct.productID,
                        colorName = cartProduct.colorName,
                        quantity = cartProduct.quantity.toInt(),
                        sizeName = cartProduct.sizeName,
                        itemCartID = cartProduct.itemID
                    )
                )
                orderPost.cartID = cartProduct.id
            }
            return orderPost
        }
    }
}