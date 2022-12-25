package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_detail_contract.*

class DetailContractActivity : BaseActivity() {

    private val fbUrl = "https://www.facebook.com/coolmate.me"
    private val instarUrl = "https://www.instagram.com/coolmate.me/"
    private val youtubeUrl = "https://www.youtube.com/channel/UCWw8wLlodKBtEvVt1tTAsMA"

    override fun layoutID(): Int = R.layout.activity_detail_contract

    override fun initData() {

    }

    override fun initView() {

    }

    override fun listenLiveData() {

    }

    override fun listeners() {
        imgFacebook.setOnClickListener {
            val i = Intent(this, WebViewActivity::class.java)
            i.putExtra("linkUrl", fbUrl)
            startActivity(i)
        }
        imgInstagram.setOnClickListener {
            val i = Intent(this, WebViewActivity::class.java)
            i.putExtra("linkUrl", instarUrl)
            startActivity(i)
        }
        imgYoutube.setOnClickListener {
            val i = Intent(this, WebViewActivity::class.java)
            i.putExtra("linkUrl", youtubeUrl)
            startActivity(i)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}