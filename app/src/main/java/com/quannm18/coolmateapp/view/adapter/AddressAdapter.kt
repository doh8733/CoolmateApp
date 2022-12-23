package com.quannm18.coolmateapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.address.Address
import kotlinx.android.synthetic.main.item_address.*

class AddressAdapter : Adapter<AddressAdapter.AddressViewHolder>() {
    private var mList: MutableList<Address> = mutableListOf()
    private val listener: MutableLiveData<Any> = MutableLiveData()

    val event: LiveData<Any> by lazy {
        listener
    }

    inner class AddressViewHolder(var itemView: View) :
        BaseViewHolder(itemView) {
        fun bind(item: Address) {
            tvAddressItem.text = item.name
            itemView.setOnClickListener {
                listener.postValue(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressAdapter.AddressViewHolder {
        return AddressViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false))
    }

    override fun onBindViewHolder(holder: AddressAdapter.AddressViewHolder, position: Int) {
        holder.bind(item = mList[position])
    }

    override fun getItemCount(): Int = mList.size

    fun initData(mList: MutableList<Address>) {
        this.mList = mList
        notifyDataSetChanged()
    }
}