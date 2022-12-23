package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_success_buying.*

class SuccessBuyingActivity : BaseActivity() {
    override fun layoutID(): Int = R.layout.activity_success_buying

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
        btnContinuesBuy.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        btnFollowBill.setOnClickListener {
            if (intent.getStringExtra("idBill") != null) {
                val intent = Intent(this, BuyingActivity::class.java)
                intent.putExtra("idBill", intent.getStringExtra("idBill"))
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, BillHomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}