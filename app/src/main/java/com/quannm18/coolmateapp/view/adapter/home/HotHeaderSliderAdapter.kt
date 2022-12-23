package com.quannm18.coolmateapp.view.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.banner.ResponseBanner
import com.quannm18.coolmateapp.utils.CommonUtils
import com.quannm18.coolmateapp.view.adapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_slider.*

class HotHeaderSliderAdapter() :
    ListAdapter<ResponseBanner, HotHeaderSliderAdapter.HotSliderHolder>(DiffItemSlider()) {
    class HotSliderHolder(itemView: View) : BaseViewHolder(itemView) {
        fun bind(item: ResponseBanner) {
            CommonUtils.loadBanner(item.path, imgItemSlider)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotSliderHolder {
        return HotSliderHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HotSliderHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class DiffItemSlider : DiffUtil.ItemCallback<ResponseBanner>() {

        override fun areContentsTheSame(
            oldItem: ResponseBanner,
            newItem: ResponseBanner
        ): Boolean = oldItem == newItem

        override fun areItemsTheSame(
            oldItem: ResponseBanner,
            newItem: ResponseBanner
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

}