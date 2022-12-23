package com.quannm18.coolmateapp.view.dialog

import android.content.Context
import androidx.lifecycle.LiveData
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseDialog
import com.quannm18.coolmateapp.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.dialog_phanhoi_khieu_nai.*

class DialogPhanHoiKhieuNai(context: Context) : BaseDialog(context) {
    companion object {
        var instance: DialogPhanHoiKhieuNai? = null
        fun newInstance(context: Context): DialogPhanHoiKhieuNai {
            if (instance == null) {
                instance = DialogPhanHoiKhieuNai(context)
            }
            return instance!!
        }
    }

    private val listener: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val event: LiveData<Boolean> by lazy {
        listener
    }

    override fun layoutID(): Int = R.layout.dialog_phanhoi_khieu_nai

    override fun listener() {
        btnCloseReportDialog.setOnClickListener {
            listener.postValue(false)
        }
        btnReportDialog.setOnClickListener {
            listener.postValue(true)
        }
    }
}