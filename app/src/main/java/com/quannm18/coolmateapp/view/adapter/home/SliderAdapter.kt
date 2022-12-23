package com.quannm18.coolmateapp.view.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.quannm18.coolmateapp.MyApp.Companion.glide
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.ItemSlider
import com.quannm18.coolmateapp.view.adapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_slider.*

class SliderAdapter : Adapter<SliderAdapter.SliderHolder>() {
    private var mList: MutableList<ItemSlider> = mutableListOf()

    class SliderHolder(itemView: View) : BaseViewHolder(itemView) {
        fun bind(item: ItemSlider) {
            glide.load(item.image).into(imgItemSlider)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderHolder {
        return SliderHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SliderHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int = mList.size

    fun initMyAdapter(mList: MutableList<ItemSlider>) {
        val diffUtil = DiffItemSlider()
        if (diffUtil.areContentsTheSame(this.mList, mList)) {
            return
        } else {
            this.mList.clear()
            this.mList.addAll(mList)
        }
    }

    class DiffItemSlider : DiffUtil.ItemCallback<MutableList<ItemSlider>>() {

        override fun areContentsTheSame(
            oldItem: MutableList<ItemSlider>,
            newItem: MutableList<ItemSlider>
        ): Boolean = oldItem == newItem

        override fun areItemsTheSame(
            oldItem: MutableList<ItemSlider>,
            newItem: MutableList<ItemSlider>
        ): Boolean {
            return oldItem.size == newItem.size && oldItem.containsAll(newItem)
        }
    }

}