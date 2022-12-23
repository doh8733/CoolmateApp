package com.quannm18.coolmateapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.android.material.imageview.ShapeableImageView
import com.quannm18.coolmateapp.MyApp
import com.quannm18.coolmateapp.MyApp.Companion.glide
import com.quannm18.coolmateapp.R


class DetailProductAdapter : Adapter<DetailProductAdapter.OrderViewHolder>() {

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

        fun bind(it: TempModel) {
            tvNameProductOrder.text = it.name
            tvSubTitleOrder.text = it.phanLoai
            tvTotalOrder.text = MyApp.dec.format(it.price)
            glide.load(it.image).into(imgItemOrder)
            tvCountOrder.text = "x${it.sl}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        cloneData()[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = cloneData().size


    fun cloneData(): List<TempModel> {
        val product1 = TempModel(
            name = "Áo Sơ mi nam Excool Limited ngắn tay chui đầu",
            price = 349000,
            phanLoai = "Đen, L",
            image = R.drawable.excoolsomiden,
            sl = 2
        )
        val product2 = TempModel(
            name = "Áo Sơ mi nam Excool Limited ngắn tay chui đầu",
            price = 349000,
            phanLoai = "Xám, M",
            image = R.drawable.excoolsomixam,
            sl = 1
        )
        val list = listOf<TempModel>(product1, product2)
        return list
    }

    data class TempModel(
        val name: String = "",
        val price: Int = 0,
        val phanLoai: String = "",
        val image: Int = 0,
        val sl: Int = 0
    )

}