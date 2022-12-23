package com.quannm18.coolmateapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.github.hariprasanths.bounceview.BounceView
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.bus.ColorBus
import com.quannm18.coolmateapp.model.product.Color
import kotlinx.android.synthetic.main.item_color.*


class DetailColorAdapter :
    ListAdapter<Color, DetailColorAdapter.DetailImageViewHolder>(ColorDiffCallback()) {
    private val listener: MutableLiveData<Any> = MutableLiveData()
    private var isNewItemChecked = false
    private var lastIndexItemChecked = -1
    val event: LiveData<Any> by lazy {
        listener
    }

    inner class DetailImageViewHolder(itemView: View) : BaseViewHolder(itemView) {
        fun bind(color: Color) {
            val resColor = try {
                android.graphics.Color.parseColor("${color.colorCode}")
            } catch (e: Exception) {
                android.graphics.Color.WHITE
            }
            cvColorItemColor.setCardBackgroundColor(resColor)
            cvSecondaryColor.setCardBackgroundColor(resColor)

            if (isNewItemChecked) {
                cvWhiteItem.visibility = View.VISIBLE
            } else {
                if (layoutPosition == 0) {
                    cvWhiteItem.visibility = View.VISIBLE
                    lastIndexItemChecked = 0
                }
            }
            if (color.isChecked){
                cvWhiteItem.visibility = View.VISIBLE
            }else{
                cvWhiteItem.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener {
                listener.postValue(color)
                handleCheck(layoutPosition)
            }

            BounceView.addAnimTo(itemView).setScaleForPopOutAnim(1.1f, 1.1f)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailImageViewHolder {
        return DetailImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailImageViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class ColorDiffCallback : DiffUtil.ItemCallback<Color>() {

        override fun areItemsTheSame(oldItem: Color, newItem: Color): Boolean =
            oldItem.colorCode == newItem.colorCode

        override fun areContentsTheSame(oldItem: Color, newItem: Color): Boolean =
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