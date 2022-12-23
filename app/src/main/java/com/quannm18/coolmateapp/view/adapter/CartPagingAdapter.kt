package com.quannm18.coolmateapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.quannm18.coolmateapp.MyApp
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.bus.CartBus
import com.quannm18.coolmateapp.bus.ItemCart
import com.quannm18.coolmateapp.model.cart.CartProduct
import com.quannm18.coolmateapp.model.itemcart.PutItemCart
import com.quannm18.coolmateapp.network.api.ApiConfig
import kotlinx.android.synthetic.main.item_cart.*

class CartPagingAdapter :
    PagingDataAdapter<CartProduct, CartPagingAdapter.CartPagingViewHolder>(Diff()) {
    private val listener: MutableLiveData<Any> by lazy {
        MutableLiveData()
    }
    val event: LiveData<Any> by lazy {
        listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartPagingViewHolder {
        return CartPagingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartPagingViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class CartPagingViewHolder(itemView: View) : BaseViewHolder(itemView) {
        fun bind(cart: CartProduct) {
            val max = cart.totalProduct
            var count = cart.quantity
            MyApp.glide.load(ApiConfig.BASE_URL + cart.linkImage).into(imgItemCart)
            tvNameProductCart.text = cart.name
            tvSubTitleCart.text = "${cart.colorName}, ${cart.sizeName}"
            tvCountCart.text = "${cart.quantity}"
            tvTotalItemCart.text = "${MyApp.dec.format(cart.totalPrice / cart.quantity)} VND"
            chkItemCart.isChecked = cart.isChecked

            chkItemCart.setOnClickListener {
                listener.postValue(CartBus(chkItemCart.isChecked, cart.productID))
            }

            viewChkItemCart.setOnClickListener {
                chkItemCart.isChecked = !chkItemCart.isChecked
                listener.postValue(CartBus(chkItemCart.isChecked, cart.productID))
            }

            btnDeleteItemCart.setOnClickListener {
                listener.postValue(cart)
            }

            itemView.setOnClickListener {
                listener.postValue(ItemCart(cart))
            }
            btnUpCart.setOnClickListener {
                if (count == max) {
                    btnUpCart.isEnabled = false
                } else {
                    count++
                }
                btnDownCart.isEnabled = true
                tvCountCart.text = "$count"
                listener.postValue(
                    PutItemCart(
                        colorName = cart.colorName,
                        sizeName = cart.sizeName,
                        quantity = count.toInt(),
                        itemCartId = cart.itemID
                    )
                )
            }
            btnDownCart.setOnClickListener {
                if (count == 1L) {
                    btnUpCart.isEnabled = false
                } else {
                    count--
                }
                btnDownCart.isEnabled = true
                tvCountCart.text = "$count"
                listener.postValue(
                    PutItemCart(
                        colorName = cart.colorName,
                        sizeName = cart.sizeName,
                        quantity = count.toInt(),
                        itemCartId = cart.itemID
                    )
                )
            }
        }
    }

    class Diff() : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean =
            oldItem == newItem
    }
}