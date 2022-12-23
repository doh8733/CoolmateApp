package com.quannm18.coolmateapp.view.adapter.spinner

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.quannm18.coolmateapp.R

class SpinnerMaterialCatalogAdapter(
    context: Context,
    resource: Int,
    objects: MutableList<String>,
    var title: String
) :
    ArrayAdapter<String>(context, resource, objects) {
    @SuppressLint("ViewHolder", "MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_selector, parent, false)
        val tvSelected: TextView by lazy { convertView.findViewById(R.id.tvTypeVoucher) }
        val tvType: TextView by lazy { convertView.findViewById(R.id.tvTypeSpn) }
        val item: String = this.getItem(position)!!
        tvSelected.text = item
        tvType.text = title
        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_catalog_category, parent, false)
        val tvViewCategory: TextView by lazy { convertView.findViewById(R.id.tvVoucher) }
        val item: String = this.getItem(position)!!
        tvViewCategory.text = item
        return convertView
    }

}