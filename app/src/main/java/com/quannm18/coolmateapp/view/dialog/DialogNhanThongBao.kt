package com.quannm18.coolmateapp.view.dialog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_log_out.*
import kotlinx.android.synthetic.main.dialog_show_validate.btnCloseDialog

class DialogNhanThongBao(context: Context) : BaseDialog(context) {
    companion object{
        var instance : DialogNhanThongBao? = null
        fun newInstance(context: Context): DialogNhanThongBao {
            if (instance == null){
                instance = DialogNhanThongBao(context)
            }
            return instance!!
        }
    }
    private val listener: MutableLiveData<Boolean> = MutableLiveData()
    val event : LiveData<Boolean> by lazy {
        listener
    }

    override fun initView() {
    }
    override fun layoutID(): Int = R.layout.dialog_nhan_thong_bao

    override fun listener() {
        btnCloseDialog.setOnClickListener {
            listener.postValue(false)
        }
        btnLogoutDialog.setOnClickListener {
            listener.postValue(true)
        }
    }
}