package com.quannm18.coolmateapp.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quannm18.coolmateapp.R

class DialogDoiTraBottomSheet : BottomSheetDialogFragment() {

    private fun initView() {
    }

    private fun listenerLiveData() {

    }

    private fun listener() {

    }


    override fun onCancel(dialog: DialogInterface) {

        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun getTheme(): Int = R.style.NoBackgroundDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_doi_tra_sheet_dialog, container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listenerLiveData()
        listener()
    }


}