package com.quannm18.coolmateapp.view.dialog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_log_out.*
import kotlinx.android.synthetic.main.dialog_show_validate.btnCloseDialog

class DialogAsk(context: Context, var text: String, var description: String) : BaseDialog(context) {
    companion object{
        var instance : DialogAsk? = null
        fun newInstance(context: Context, text: String, description: String): DialogAsk {
            if (instance == null){
                instance = DialogAsk(context, text, description)
            }
            return instance!!
        }
    }
    private val listener: MutableLiveData<Boolean> = MutableLiveData()
    val event : LiveData<Boolean> by lazy {
        listener
    }

    override fun initView() {
        textView15.text = text
        btnLogoutDialog.text = text
        textView18.text = description
    }
    override fun layoutID(): Int = R.layout.dialog_log_out

    override fun listener() {
        btnCloseDialog.setOnClickListener {
            listener.postValue(false)
            dismiss()
        }
        btnLogoutDialog.setOnClickListener {
            listener.postValue(true)
            dismiss()

        }
    }
}