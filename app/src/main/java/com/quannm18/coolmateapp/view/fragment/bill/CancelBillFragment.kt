package com.quannm18.coolmateapp.view.fragment.bill

import androidx.recyclerview.widget.LinearLayoutManager
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseFragment
import com.quannm18.coolmateapp.view.adapter.BillAdapter
import kotlinx.android.synthetic.main.fragment_cancel_bill.*

class CancelBillFragment : BaseFragment() {
    private val billAdapter: BillAdapter = BillAdapter()
    override fun layoutID(): Int = R.layout.fragment_cancel_bill

    override fun initData() {

    }

    override fun initView() {
        rcvCancelBill.apply {
            adapter = billAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun listenLiveData() {
        billAdapter.event.observe(viewLifecycleOwner) {
//            DialogCancelCartBottomSheet().show(
//                requireActivity().supportFragmentManager,
//                DialogDetailAddToCartBottomSheet::class.simpleName.toString()
//            )

        }
    }

    override fun listener() {

    }

}