package com.quannm18.coolmateapp.view.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.rss.Item
import com.quannm18.coolmateapp.utils.CommonUtils
import kotlinx.android.synthetic.main.item_explorer.view.*

class ExplorerAdapter(
    context: Context,
    var onClickViewMore: (Item) -> Unit,
) :
    RecyclerView.Adapter<ExplorerAdapter.ExplorerHolder>() {
    private var listExplorer: List<Item> = mutableListOf()

    class ExplorerHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.imgExplorer
        private val name = view.tvBrands
        private val title = view.tvMade
        private val summary = view.tvDes
        fun onBindViews(item: Item) {
            name.text = item.author.name
            title.text = item.title
            item.summary.replace("[&#8230;]","").let {
                val positionStart = it.indexOf(">") + 1
                val positionEnd = it.indexOf("</p>")
                summary.text = it.substring(positionStart, positionEnd)+"..."
            }
            CommonUtils.loadAvatarInt(R.drawable.bg_item_explorer,image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExplorerHolder {
        return ExplorerHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_explorer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ExplorerHolder, position: Int) {
        val item = listExplorer[position]

        holder.onBindViews(item)
        holder.itemView.btnViewMore.setOnClickListener {
            onClickViewMore(item)
        }
    }

    override fun getItemCount(): Int {
        return listExplorer.size
    }

    fun setData(list: List<Item>) {
        this.listExplorer = list
        notifyDataSetChanged()
    }
}