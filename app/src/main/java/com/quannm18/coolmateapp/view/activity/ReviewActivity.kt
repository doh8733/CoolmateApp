package com.quannm18.coolmateapp.view.activity

import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : BaseActivity() {
    override fun layoutID(): Int = R.layout.activity_review

    override fun initData() {

    }

    override fun initView() {
        marginStatusBar(listOf(imgBackReview))
    }

    override fun listenLiveData() {

    }

    override fun listeners() {

    }

}