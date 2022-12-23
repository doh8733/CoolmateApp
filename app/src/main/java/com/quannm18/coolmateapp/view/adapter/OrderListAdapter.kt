package com.quannm18.coolmateapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.imageview.ShapeableImageView
import com.quannm18.coolmateapp.MyApp
import com.quannm18.coolmateapp.MyApp.Companion.glide
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.cart.CartProduct
import com.quannm18.coolmateapp.network.api.ApiConfig.Companion.BASE_URL


class OrderListAdapter : PagingDataAdapter<CartProduct, OrderListAdapter.OrderViewHolder>(Diff()) {

    private val mListener: MutableLiveData<Any> = MutableLiveData()
    val event: LiveData<Any> by lazy {
        mListener
    }

    inner class OrderViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val imgItemOrder: ShapeableImageView by lazy {
            itemView.findViewById<ShapeableImageView>(
                R.id.imgItemOrder
            )
        }
        private val tvNameProductOrder: TextView by lazy { itemView.findViewById<TextView>(R.id.tvNameProductOrder) }
        private val tvSubTitleOrder: TextView by lazy { itemView.findViewById<TextView>(R.id.tvSubTitleOrder) }
        private val tvTotalOrder: TextView by lazy { itemView.findViewById<TextView>(R.id.tvTotalOrder) }
        private val tvCountOrder: TextView by lazy { itemView.findViewById<TextView>(R.id.tvCountOrder) }

        fun bind(cart: CartProduct) {
            tvNameProductOrder.text = cart.name
            tvSubTitleOrder.text = "${cart.colorName}, ${cart.sizeName}"
            tvTotalOrder.text = MyApp.dec.format(cart.totalPrice / cart.quantity) + " VND"
            glide.load(BASE_URL + cart.linkImage).into(imgItemOrder)
            tvCountOrder.text = "x${cart.quantity}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        )
    }

    class Diff : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean =
            oldItem == newItem

    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}