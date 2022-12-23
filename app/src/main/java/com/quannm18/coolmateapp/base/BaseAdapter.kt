package com.quannm18.coolmateapp.view.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.quannm18.coolmateapp.utils.CommonUtils
import kotlinx.android.extensions.LayoutContainer


open class BaseViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView),
    LayoutContainer {

    fun loadAppIcon(packageName: String, view: ImageView) {
        CommonUtils.loadIconApp(packageName, view)
    }

    fun loadItem(link: String, view: ImageView) {
        CommonUtils.loadAvatarItem(link, view)
    }

    override val containerView: View
        get() = itemView
}

