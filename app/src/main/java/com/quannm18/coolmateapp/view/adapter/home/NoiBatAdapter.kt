package com.quannm18.coolmateapp.view.adapter.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.quannm18.coolmateapp.MyApp.Companion.dec
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.product.Product
import com.quannm18.coolmateapp.utils.CommonUtils
import com.quannm18.coolmateapp.view.adapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_home.*


class NoiBatAdapter(
    var likeItem: (Product) -> Unit,
    var clickItem: (Product) -> Unit
) : ListAdapter<Product, NoiBatAdapter.NoiBatViewHolder>(Diff()) {

    inner class NoiBatViewHolder(itemView: View) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Product) {
            CommonUtils.loadAvatarItem(item.image[0] ?: "", imgItemHome)
            tvNameItemHome.text = item.productName
            tvPriceItemHome.text = "${dec.format(item.sellingPrice)} VND"

            btnHeartItemHome.setOnClickListener {
                likeItem(item)
            }

            itemView.setOnClickListener {
                clickItem(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoiBatViewHolder {
        return NoiBatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoiBatViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class Diff() : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean = oldItem == newItem
    }
}