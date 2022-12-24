package com.quannm18.coolmateapp.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.enum.MyEnum
import com.quannm18.coolmateapp.model.order.ShippingStatus
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.SingleLiveEvent
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.utils.Status.*
import com.quannm18.coolmateapp.viewmodel.CartViewModel
import com.quannm18.coolmateapp.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_huy_don_bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.fragment_huy_don_bottom_sheet_dialog.view.*

class DialogCancelCartBottomSheet : BottomSheetDialogFragment() {
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val cartViewModel :CartViewModel by activityViewModels()
    private val listener: SingleLiveEvent<Any> = SingleLiveEvent()
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    var stt: String = ""

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
        view.btnAgree.setOnClickListener {
            val selected = rdoGroupAnswer.checkedRadioButtonId
            val rdo :RadioButton= view.findViewById(selected)
            if (rdo.id == R.id.rdoAnswer7){
                huyDonHang("63a6b4ca345ac4a37eddeba2",MyEnum.ShippingStatus.DATRA,edtAnswer.text.toString())
                return@setOnClickListener
            }
            huyDonHang("63a6b4ca345ac4a37eddeba2",MyEnum.ShippingStatus.CHOXACNHANHUYDONHANG,rdo.text.toString())
            Toast.makeText(requireContext(), rdo.text, Toast.LENGTH_SHORT).show()
        }

    }

    private fun huyDonHang(id:String,shippingStatus: String ,note :String){
        cartViewModel.huyDonHang("Bearer ${sessionManager.fetchAuthToken()}",id,shippingStatus, note).observe(viewLifecycleOwner){
            it?.let {
                when(it.status){
                    ERROR ->   {
                        Log.e("TAG", "huyDonHang: ${it.message}", )
                    }
                    SUCCESS -> {
                        it.data?.let {
                            if (it.code() == 200){
                                Toast.makeText(requireContext(), "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show()
                                Log.e("TAG", "huyDonHang: ${it.body()}", )
                            }else{
                                Toast.makeText(requireContext(), "Hủy đơn hàng thất bại", Toast.LENGTH_SHORT).show()
                                Log.e("TAG", "huyDonHang: ${it.message()}", )

                            }
                        }
                    }
                    LOADING -> {
                        Toast.makeText(requireContext(), "doi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

}