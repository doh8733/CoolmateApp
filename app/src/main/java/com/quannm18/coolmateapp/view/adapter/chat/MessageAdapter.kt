package com.quannm18.coolmateapp.view.adapter.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.utils.DateUtil
import com.sendbird.android.BaseMessage
import com.sendbird.android.SendBird
import com.sendbird.android.UserMessage
import kotlinx.android.synthetic.main.item_chat_other.view.*
import kotlinx.android.synthetic.main.item_text_chat_me.view.*

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_USER_MESSAGE_ME = 10
        private const val VIEW_TYPE_USER_MESSAGE_OTHER = 11
        const val PUT_DATA_IMG = "IMG"
    }

    private var messages: MutableList<BaseMessage>
    var img: String = ""

    init {
        messages = ArrayList()
    }

    fun loadMessages(messages: MutableList<BaseMessage>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    fun addFirst(message: BaseMessage) {
        messages.add(0, message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER_MESSAGE_ME -> {
                MyTextChatHolder(layoutInflater.inflate(R.layout.item_text_chat_me, parent, false))
            }
            VIEW_TYPE_USER_MESSAGE_OTHER -> {
                OtherTextChatHolder(layoutInflater.inflate(R.layout.item_chat_other, parent, false))
            }
            else -> MyTextChatHolder(
                layoutInflater.inflate(
                    R.layout.item_text_chat_me,
                    parent,
                    false
                )
            )
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (val message = messages[position]) {
            is UserMessage -> {
                return if (message.sender.userId.equals(SendBird.getCurrentUser().userId)) VIEW_TYPE_USER_MESSAGE_ME
                else VIEW_TYPE_USER_MESSAGE_OTHER
            }
            else -> return -1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_USER_MESSAGE_ME -> {
                holder as MyTextChatHolder
                holder.onBindViews(messages[position] as UserMessage)
            }
            VIEW_TYPE_USER_MESSAGE_OTHER -> {
                holder as OtherTextChatHolder
                holder.onBindViews(img, messages[position] as UserMessage)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MyTextChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText = view.tvChat_me
        fun onBindViews(message: UserMessage) {
            messageText.text = message.message
        }
    }

    class OtherTextChatHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val messageOthertext = view.tvOtherChat
        val time = view.tvTime
        val imageUser = view.imageUser

        fun onBindViews(img: String, message: UserMessage) {
            messageOthertext.text = message.message
            time.text =
                "${DateUtil.formatDate(message.createdAt)} ${DateUtil.formatTime(message.createdAt)}"
            Glide.with(view.context)
                .load(img)
                .apply(RequestOptions())
                .transform(CenterCrop())
                .transform(RoundedCorners(74))
                .into(imageUser)
            Log.e("IMG", "onBindViews: $img")
        }
    }
}