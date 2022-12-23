package com.quannm18.coolmateapp.view.activity

import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import com.quannm18.coolmateapp.view.adapter.chat.MessageAdapter
import com.sendbird.android.*
import kotlinx.android.synthetic.main.activity_channel.*


class ChannelActivity : BaseActivity() {
    private val mAdapter: MessageAdapter by lazy {
        MessageAdapter()
    }
    private lateinit var groupChannel: GroupChannel
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }

    override fun layoutID(): Int = R.layout.activity_channel

    override fun initData() {
        setUpRecyclerView()
        setButtonListeners()
        customColorStatusBar()
    }

    override fun initView() {
        marginNavigationBar(listOf(tilSend))
    }

    override fun listenLiveData() {

    }

    override fun listeners() {
        Log.e("URL", "onResume: ${managerSaveLocal.getChatLink()}")
        GroupChannel.getChannel(managerSaveLocal.getChatLink(),
            GroupChannel.GroupChannelGetHandler { groupChannel, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@GroupChannelGetHandler
                }
                this.groupChannel = groupChannel
                getMessages()
                for (i in groupChannel.members.indices) {
                    tvNameUser.text = groupChannel.members[i].nickname
                    if (groupChannel.members[i].connectionStatus.toString()
                            .contains("ONLINE", true)
                    ) {
                        tvStatus.text = "Online"
                    } else if (groupChannel.members[i].connectionStatus.toString()
                            .contains("OFFLINE", true)
                    ) {
                        tvStatus.text = "Offline"
                    }
                    mAdapter.img = groupChannel.members[i].profileUrl
                    Glide.with(this)
                        .load(groupChannel.members[i].profileUrl)
                        .apply(RequestOptions())
                        .transform(CenterCrop())
                        .transform(RoundedCorners(72))
                        .into(imgUser)
                }
            })
        SendBird.addChannelHandler(
            CHANNEL_HANDLER_ID,
            object : SendBird.ChannelHandler() {
                override fun onMessageReceived(
                    baseChannel: BaseChannel,
                    baseMessage: BaseMessage
                ) {
                    if (baseChannel.url.equals(managerSaveLocal.getChatLink())) {
                        mAdapter.addFirst(baseMessage)
                        groupChannel.markAsRead()
                    }
                }
            })
    }

    private fun setButtonListeners() {
        imgBackChat.setOnClickListener {
            finish()
        }

        imgIconSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun setUpRecyclerView() {
        val mLayoutManager = LinearLayoutManager(this@ChannelActivity)
        mLayoutManager.reverseLayout = true
        rcvChatChannel.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
            scrollToPosition(0)
        }
    }

    override fun onDestroy() {
        SendBird.removeChannelHandler(CHANNEL_HANDLER_ID)
        super.onDestroy()
    }


    private fun sendMessage() {
        val params = UserMessageParams()
            .setMessage(tilSend.editText?.text.toString())

        if (tilSend.editText?.text.toString().isEmpty()) {
            return
        }
        groupChannel.sendUserMessage(params, object : BaseChannel.SendUserMessageHandler {
            override fun onSent(p0: UserMessage?, p1: SendBirdException?) {
                if (p1 != null) {    // Error.
                    return
                } else {
                    p0?.let { mAdapter.addFirst(it) }
                    tilSend.editText?.text?.clear()
                }
            }
        })
    }


    private fun getMessages() {
        val previousMessageListQuery = groupChannel.createPreviousMessageListQuery()
        previousMessageListQuery.load(
            100,
            true
        ) { messages, e ->
            if (e != null) {
                Log.e("Error", "Error" + e.message.toString())
            }
            mAdapter.loadMessages(messages!!)
        }

    }

    private fun customColorStatusBar() {
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    companion object {
        private const val CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_CHAT"
    }
}