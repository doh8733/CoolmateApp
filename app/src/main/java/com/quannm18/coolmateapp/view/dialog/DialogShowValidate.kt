package com.quannm18.coolmateapp.view.dialog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseDialog
import com.quannm18.coolmateapp.bus.ListenerValidate
import kotlinx.android.synthetic.main.dialog_show_validate.*

class DialogShowValidate(context: Context,var listenerValidate: ListenerValidate) : BaseDialog(context) {
    companion object{
        var instance : DialogShowValidate? = null
        fun newInstance(context: Context, listenerValidate: ListenerValidate): DialogShowValidate {
            if (instance == null){
                instance = DialogShowValidate(context, listenerValidate)
            }
            return instance!!
        }
    }
    private val listener: MutableLiveData<Any> = MutableLiveData()
    val event : LiveData<Any> by lazy {
        listener
    }

    override fun initView() {
        tvDescriptionValidate.text = listenerValidate.message
    }
    override fun layoutID(): Int = R.layout.dialog_show_validate

    override fun listener() {
        btnCloseDialog.setOnClickListener {
            dismiss()
        }
        btnRetryLogin.setOnClickListener {
            dismiss()
        }
    }
}