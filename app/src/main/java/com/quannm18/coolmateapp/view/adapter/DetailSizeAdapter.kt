package com.quannm18.coolmateapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.github.hariprasanths.bounceview.BounceView
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.product.Size
import kotlinx.android.synthetic.main.item_size.*


class DetailSizeAdapter :
    ListAdapter<Size, DetailSizeAdapter.DetailImageViewHolder>(SizeDiffCallback()) {
    private var isNewItemChecked = false
    private var lastIndexItemChecked = -1
    private val listener: MutableLiveData<Any> = MutableLiveData()
    val event: LiveData<Any> by lazy {
        listener
    }

    inner class DetailImageViewHolder(itemView: View) : BaseViewHolder(itemView) {
        fun bind(size: Size) {
            btnSizeDetail.text = size.name

            if (isNewItemChecked) {
                btnSizeDetail.setBackgroundResource(R.drawable.custom_bg_select_size)
                btnSizeDetail.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                if (layoutPosition == 0) {
                    btnSizeDetail.setBackgroundResource(R.drawable.custom_bg_select_size)
                    btnSizeDetail.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.white
                        )
                    )
                    lastIndexItemChecked = 0
                }
            }
            if (size.isChecked) {
                btnSizeDetail.setBackgroundResource(R.drawable.custom_bg_select_size)
                btnSizeDetail.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                btnSizeDetail.setBackgroundResource(R.drawable.custom_bg_unselect_size)
                btnSizeDetail.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            }

            itemView.setOnClickListener {
                listener.postValue(size)
                handleCheck(layoutPosition)
            }

            BounceView.addAnimTo(itemView).setScaleForPopOutAnim(1.1f, 1.1f)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailImageViewHolder {
        return DetailImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_size, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailSizeAdapter.DetailImageViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class SizeDiffCallback : DiffUtil.ItemCallback<Size>() {

        override fun areItemsTheSame(oldItem: Size, newItem: Size): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Size, newItem: Size): Boolean =
            oldItem == newItem
    }

    fun handleCheck(index: Int) {
        isNewItemChecked = true
        getItem(lastIndexItemChecked).isChecked = false
        getItem(index).isChecked = true
        lastIndexItemChecked = index
        notifyDataSetChanged()
    }
}