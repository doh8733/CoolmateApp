package com.quannm18.coolmateapp.view.dialog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_log_out.*
import kotlinx.android.synthetic.main.dialog_show_validate.btnCloseDialog

class DialogFailAnim(context: Context, var text: String, var description: String) : BaseDialog(context) {
    companion object{
        var instance : DialogFailAnim? = null
        fun newInstance(context: Context, text: String, description: String): DialogFailAnim {
            if (instance == null){
                instance = DialogFailAnim(context, text, description)
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
//        btnLogoutDialog.text = text
        textView18.text = description
    }
    override fun layoutID(): Int = R.layout.dialog_failed

    override fun listener() {
        btnCloseDialog.setOnClickListener {
            listener.postValue(false)
        }
        btnLogoutDialog.setOnClickListener {
            listener.postValue(true)
        }
    }
}