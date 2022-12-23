package com.quannm18.coolmateapp.view.dialog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_log_out.*
import kotlinx.android.synthetic.main.dialog_show_validate.btnCloseDialog

class DialogLogOut(context: Context) : BaseDialog(context) {
    companion object{
        var instance : DialogLogOut? = null
        fun newInstance(context: Context): DialogLogOut {
            if (instance == null){
                instance = DialogLogOut(context)
            }
            return instance!!
        }
    }
    private val listener: MutableLiveData<Boolean> = MutableLiveData()
    val event : LiveData<Boolean> by lazy {
        listener
    }
    override fun layoutID(): Int = R.layout.dialog_log_out

    override fun listener() {
        btnCloseDialog.setOnClickListener {
            listener.postValue(false)
        }
        btnLogoutDialog.setOnClickListener {
            listener.postValue(true)
        }
    }
}