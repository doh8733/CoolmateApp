package com.quannm18.coolmateapp.view.activity

import com.google.android.material.tabs.TabLayoutMediator
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.view.adapter.home.BillViewPagerAdapter
import kotlinx.android.synthetic.main.activity_bill_home.*

class BillHomeActivity : BaseActivity() {
    private lateinit var mAdapter: BillViewPagerAdapter

    override fun layoutID(): Int = R.layout.activity_bill_home

    override fun initData() {
        mAdapter = BillViewPagerAdapter(this)
    }

    override fun initView() {
        marginStatusBar(listOf(toolbar))
        paddingRootView(toolbar)
        vpMainHome.apply {
            adapter = mAdapter
            offscreenPageLimit = 4
        }
        TabLayoutMediator(tabLayout, vpMainHome) { a, b ->
            when (b) {
                0 -> {
                    a.text = getString(R.string.xac_nhan)
                }
                1 -> {
                    a.text = getString(R.string.dang_giao)
                }
                2 -> {
                    a.text = getString(R.string.da_giao)
                }
                3 -> {
                    a.text = getString(R.string.da_huy)
                }
            }
        }.attach()

    }

    override fun listenLiveData() {

    }

    override fun listeners() {
        btnBackBillHome.setOnClickListener {
            finish()
        }
    }

}