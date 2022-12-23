package com.quannm18.coolmateapp.bus

import com.quannm18.coolmateapp.model.cart.CartProduct

data class ItemCart(
    var cart: CartProduct = CartProduct()
)