package com.quannm18.coolmateapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.cart.Cart
import com.quannm18.coolmateapp.utils.CommonUtils
import kotlinx.android.synthetic.main.item_cart.*

class CartAdapter : ListAdapter<Cart, CartAdapter.CartViewHolder>(Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }

    }

    inner class CartViewHolder(itemView: View) : BaseViewHolder(itemView) {
        fun bind(cart: Cart) {
            CommonUtils.loadAvatarItem(cart.products.product.image[0] ?: "", imgItemCart)
            tvNameProductCart.text = cart.name
            tvSubTitleCart.text = "${cart.products.colorName},${cart.products.sizeName}"
            tvCountCart.text = "${cart.products.quantity}"
        }
    }

    class Diff() : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean = oldItem == newItem
    }
}