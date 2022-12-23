package com.quannm18.coolmateapp.view.dialog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_add_voucher.*

class DialogAddVoucher(context: Context) : BaseDialog(context) {
    companion object {
        var instance: DialogAddVoucher? = null
        fun newInstance(context: Context): DialogAddVoucher {
            if (instance == null) {
                instance = DialogAddVoucher(context)
            }
            return instance!!
        }
    }

    private val listener: MutableLiveData<Boolean> = MutableLiveData()
    val event: LiveData<Boolean> by lazy {
        listener
    }

    override fun layoutID(): Int = R.layout.dialog_add_voucher

    override fun listener() {
        imgClose.setOnClickListener {
            listener.postValue(false)
        }
        btnAddVoucher.setOnClickListener {
            listener.postValue(true)
        }
    }
}