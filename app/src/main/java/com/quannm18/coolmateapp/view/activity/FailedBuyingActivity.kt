package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_failed_buying.*

class FailedBuyingActivity : BaseActivity() {
    override fun layoutID(): Int = R.layout.activity_failed_buying

    override fun initData() {

    }

    override fun initView() {

    }

    override fun listenLiveData() {

    }

    override fun listeners() {
        btnBackHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        btnBack.setOnClickListener {
            finish()
        }
        btnRetry.setOnClickListener {
            finish()
        }
    }
}