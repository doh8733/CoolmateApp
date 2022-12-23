package com.quannm18.coolmateapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.quannm18.coolmateapp.MyApp.Companion.glide
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.utils.CommonUtils
import kotlinx.android.synthetic.main.item_detail_product.*


class DetailImageAdapter :
    ListAdapter<String, DetailImageAdapter.DetailImageViewHolder>(ImageDiffCallback()) {

    private val listener: MutableLiveData<Any> = MutableLiveData()
    val event: LiveData<Any> by lazy {
        listener
    }

    inner class DetailImageViewHolder(itemView: View) : BaseViewHolder(itemView) {
        fun bind(linkImage: String) {
            CommonUtils.loadAvatarItem(linkImage,imgItemDetail)

            itemView.setOnClickListener {
                listener.postValue(linkImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailImageViewHolder {
        return DetailImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_detail_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailImageViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class ImageDiffCallback : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }
}