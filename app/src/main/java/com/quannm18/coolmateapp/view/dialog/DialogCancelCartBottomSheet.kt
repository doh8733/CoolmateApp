package com.quannm18.coolmateapp.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.SingleLiveEvent
import com.quannm18.coolmateapp.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_huy_don_bottom_sheet_dialog.*

class DialogCancelCartBottomSheet : BottomSheetDialogFragment() {
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val listener: SingleLiveEvent<Any> = SingleLiveEvent()
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }

    private fun initView() {

    }

    private fun listenerLiveData() {

    }

    private fun listener() {
        btnAgree.setOnClickListener {

        }
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
            R.layout.fragment_huy_don_bottom_sheet_dialog, container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listenerLiveData()
        listener()
    }


}