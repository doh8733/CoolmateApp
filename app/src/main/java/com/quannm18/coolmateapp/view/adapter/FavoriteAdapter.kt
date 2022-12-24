package com.quannm18.coolmateapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.quannm18.coolmateapp.MyApp
import com.quannm18.coolmateapp.MyApp.Companion.glide
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.favorite.ResponseFavorite
import com.quannm18.coolmateapp.network.api.ApiConfig
import com.quannm18.coolmateapp.utils.CommonUtils
import kotlinx.android.synthetic.main.item_favorite_adapter.*

class FavoriteAdapter(
    var onSelectItem: (ResponseFavorite) -> Unit,
    var onDeleteResponseFavorite: (ResponseFavorite) -> Unit
) : ListAdapter<ResponseFavorite, FavoriteAdapter.ResponseFavoriteViewHolder>(Diff()) {
    inner class ResponseFavoriteViewHolder(view: View) : BaseViewHolder(view) {
        fun onBindViews(favorite: ResponseFavorite) {
            favorite.product.color[0].image?.let {
                CommonUtils.loadAvatarItem(it[0],imgItemFavorite)
            }
            tvPrice.text = "${MyApp.dec.format(favorite.product.promotionalPrice)} VND"
            tvNameProduct.text = favorite.product.productName
            tvLocalBrand.text = favorite.product.brand

            imgNext.setOnClickListener {
                onSelectItem(favorite)
            }
            itemView.setOnClickListener {
                onSelectItem(favorite)
            }
            itemView.setOnLongClickListener {
                onDeleteResponseFavorite(favorite)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseFavoriteViewHolder {
        return ResponseFavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_adapter, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ResponseFavoriteViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.onBindViews(item)
        }
    }

    class Diff() : DiffUtil.ItemCallback<ResponseFavorite>() {
        override fun areItemsTheSame(
            oldItem: ResponseFavorite,
            newItem: ResponseFavorite
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ResponseFavorite,
            newItem: ResponseFavorite
        ): Boolean = oldItem == newItem
    }
}