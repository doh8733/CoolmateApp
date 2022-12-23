package com.quannm18.coolmateapp.view.activity

import android.view.View
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : BaseActivity() {
    private var isPayment = 1
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }

    override fun layoutID(): Int = R.layout.activity_payment

    override fun initData() {
        isPayment = if (managerSaveLocal.getPaymentMethod() == -1) {
            1
        } else {
            managerSaveLocal.getPaymentMethod()
        }
    }

    override fun initView() {
        marginStatusBar(listOf(imgBackPayment))
        marginNavigationBar(listOf(btnAddVoucher))
        isPaymentMethod(isPayment)
        rdoZaloPay.isChecked = true
        rdoThanhToanKhiNhan.isChecked = true
    }

    override fun listenLiveData() {

    }

    override fun listeners() {
        viewZaloPay.setOnClickListener {
            isPaymentMethod(0)
        }
        viewRdoZaloPay.setOnClickListener {
            isPaymentMethod(0)
        }

        viewTTKNH.setOnClickListener {
            isPaymentMethod(1)
        }
        viewRdoTTKNH.setOnClickListener {
            isPaymentMethod(1)
        }
        btnAddVoucher.setOnClickListener {
            finish()
        }
        imgBackPayment.setOnClickListener {
            finish()
        }
    }

    private fun isPaymentMethod(isPayment: Int) {
        when (isPayment) {
            0 -> {
                rdoZaloPay.visibility = View.VISIBLE
                viewRdoZaloPay.visibility = View.VISIBLE
                rdoThanhToanKhiNhan.visibility = View.INVISIBLE
                viewRdoTTKNH.visibility = View.INVISIBLE
                managerSaveLocal.savePaymentMethod(0)
            }
            1 -> {
                rdoZaloPay.visibility = View.INVISIBLE
                viewRdoZaloPay.visibility = View.INVISIBLE
                rdoThanhToanKhiNhan.visibility = View.VISIBLE
                viewRdoTTKNH.visibility = View.VISIBLE
                managerSaveLocal.savePaymentMethod(1)
            }
        }
    }
}