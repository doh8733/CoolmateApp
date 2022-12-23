package com.quannm18.coolmateapp.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.quannm18.coolmateapp.R

class LoadingDialog(var context: Context) {
    private val builder: Dialog by lazy {
        Dialog(context)
    }

    fun startLoadingDialog() {
        builder.setContentView(R.layout.custom_progress)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.setCancelable(true)
        builder.show()
    }

    fun dismissDialog() {
        builder.dismiss()
    }
}