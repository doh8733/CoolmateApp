package com.quannm18.coolmateapp.view.dialog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_huy_don_hang.*

class DialogHuyDon(context: Context) : BaseDialog(context) {
    companion object {
        var instance: DialogHuyDon? = null
        fun newInstance(context: Context): DialogHuyDon {
            if (instance == null) {
                instance = DialogHuyDon(context)
            }
            return instance!!
        }
    }

    private val listener: MutableLiveData<Boolean> = MutableLiveData()
    val event: LiveData<Boolean> by lazy {
        listener
    }

    override fun layoutID(): Int = R.layout.dialog_huy_don_hang

    override fun listener() {
        btnCloseDialog.setOnClickListener {
            listener.postValue(false)
        }
        btnCancelBillDialog.setOnClickListener {
            listener.postValue(true)
        }
    }
}