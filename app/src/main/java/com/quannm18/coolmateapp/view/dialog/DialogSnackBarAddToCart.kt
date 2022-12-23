package com.quannm18.coolmateapp.view.dialog

import android.content.Context
import android.content.Intent
import android.view.Gravity
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseDialog
import com.quannm18.coolmateapp.view.activity.BuyingActivity
import kotlinx.android.synthetic.main.snack_bar_add_to_cart.*

class DialogSnackBarAddToCart(context: Context, var idBill: String) : BaseDialog(context) {
    companion object {
        var instance: DialogSnackBarAddToCart? = null
        fun newInstance(context: Context, idBill: String): DialogSnackBarAddToCart {
            if (instance == null) {
                instance = DialogSnackBarAddToCart(context, idBill)
            }
            return instance!!
        }
    }

    override fun initView() {
        this.window?.attributes?.gravity = Gravity.TOP
    }

    override fun layoutID(): Int = R.layout.snack_bar_add_to_cart

    override fun listener() {
        itemSnackBar.setOnClickListener {
            val intent = Intent(context, BuyingActivity::class.java)
            intent.putExtra("idBill", idBill)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
            dismiss()
        }
        btnCloseSnackBar.setOnClickListener {
            dismiss()
        }
    }
}