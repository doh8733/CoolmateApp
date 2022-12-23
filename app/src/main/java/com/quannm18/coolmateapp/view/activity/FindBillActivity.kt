package com.quannm18.coolmateapp.view.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.view.adapter.BillAdapter
import kotlinx.android.synthetic.main.activity_find_bill.*

class FindBillActivity : BaseActivity() {
    override fun layoutID(): Int = R.layout.activity_find_bill

    override fun initData() {

    }

    override fun initView() {
        marginStatusBar(listOf(imgBack))
        rcvViewFind.apply {
            adapter = BillAdapter()
            layoutManager = LinearLayoutManager(this@FindBillActivity)
        }
    }

    override fun listenLiveData() {

    }

    override fun listeners() {

    }

}