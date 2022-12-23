package com.quannm18.coolmateapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.cart.Cart
import com.quannm18.coolmateapp.model.voucher.ResponseVoucher


class VoucherAdapter : ListAdapter<ResponseVoucher,VoucherAdapter.BillViewHolder>(Diff()) {
    private val mListener: MutableLiveData<Any> = MutableLiveData()
    val event: LiveData<Any> by lazy {
        mListener
    }

    inner class BillViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val tvTitleGiam: TextView by lazy { itemView.findViewById<TextView>(R.id.tvTitleGiam) }
        private val tvDesItemVoucher: TextView by lazy { itemView.findViewById<TextView>(R.id.tvDesItemVoucher) }
        private val btnTime: TextView by lazy { itemView.findViewById<TextView>(R.id.btnTime) }

        fun bind(it: ResponseVoucher) {
            tvTitleGiam.text = "Giảm ${it.value}%"
            tvDesItemVoucher.text = it.description
            btnTime.text = "Số lượng còn ${it.used} giờ"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        return BillViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_voucher, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class Diff() : DiffUtil.ItemCallback<ResponseVoucher>() {
        override fun areItemsTheSame(oldItem: ResponseVoucher, newItem: ResponseVoucher): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ResponseVoucher, newItem: ResponseVoucher): Boolean = oldItem == newItem
    }
//
//    fun cloneData(): List<TempModel2> {
//        val product1 = TempModel2(
//            name = "Giảm",
//            time = 5,
//            des = "Đơn tối thiều 200k giảm tối đa 100k",
//            giam = 10
//        )
//        val product2 = TempModel2(
//            name = "Giảm",
//            time = 1,
//            des = "Đơn tối thiều 300k giảm tối đa 200k",
//            giam = 5
//        )
//        val product3 = TempModel2(
//            name = "Giảm",
//            time = 3,
//            des = "Đơn tối thiều 200k giảm tối đa 100k",
//            giam = 10
//        )
//        val product4 = TempModel2(
//            name = "Giảm",
//            time = 5,
//            des = "Đơn tối thiều 200k giảm tối đa 100k",
//            giam = 10
//        )
//        val product5 = TempModel2(
//            name = "Giảm",
//            time = 5,
//            des = "Đơn tối thiều 200k giảm tối đa 100k",
//            giam = 10
//        )
//        val list = listOf<TempModel2>(product1, product2,product3, product4, product5)
//        return list
//    }

//    data class TempModel2(
//        val name: String = "",
//        val time: Int = 0,
//        val des: String = "",
//        val giam : Int = 0
//    )

}