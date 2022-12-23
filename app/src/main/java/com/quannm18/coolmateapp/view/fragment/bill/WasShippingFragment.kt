package com.quannm18.coolmateapp.view.fragment.bill

import androidx.recyclerview.widget.LinearLayoutManager
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseFragment
import com.quannm18.coolmateapp.view.adapter.BillAdapter
import com.quannm18.coolmateapp.view.dialog.DialogAddVoucher
import com.quannm18.coolmateapp.view.dialog.DialogCancelCartBottomSheet
import com.quannm18.coolmateapp.view.dialog.DialogDetailAddToCartBottomSheet
import com.quannm18.coolmateapp.view.dialog.DialogHuyDon
import kotlinx.android.synthetic.main.fragment_was_shipping.*

class WasShippingFragment : BaseFragment() {
    private val billAdapter: BillAdapter = BillAdapter()
    override fun layoutID(): Int = R.layout.fragment_was_shipping

    override fun initData() {
    }

    override fun initView() {
        rcvBillWasShipping.apply {
            adapter = billAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun listenLiveData() {
        billAdapter.event.observe(viewLifecycleOwner){
//            DialogCancelCartBottomSheet().show(
//                requireActivity().supportFragmentManager,
//                DialogDetailAddToCartBottomSheet::class.simpleName.toString()
//            )

        }
    }

    override fun listener() {

    }

}