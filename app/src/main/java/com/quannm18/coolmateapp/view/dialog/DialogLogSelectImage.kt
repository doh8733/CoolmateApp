package com.quannm18.coolmateapp.view.dialog

import android.content.Context
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseDialog
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.dialog_log_out.*
import kotlinx.android.synthetic.main.dialog_show_validate.btnCloseDialog

class DialogLogSelectImage(context: Context) : BaseDialog(context) {
    companion object{
        var instance : DialogLogSelectImage? = null
        fun newInstance(context: Context): DialogLogSelectImage {
            if (instance == null){
                instance = DialogLogSelectImage(context)
            }
            return instance!!
        }
    }
    private lateinit var imgUri: Uri

    private val listener: MutableLiveData<Boolean> = MutableLiveData()
    val event : LiveData<Boolean> by lazy {
        listener
    }
    override fun layoutID(): Int = R.layout.dialog_upload_image

    override fun listener() {
        btnCloseDialog.setOnClickListener {
            dismiss()
        }
        btnLogoutDialog.setOnClickListener {
            listener.postValue(true)
        }
    }
}