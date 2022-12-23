package com.quannm18.coolmateapp.view.fragment.bill

import androidx.recyclerview.widget.LinearLayoutManager
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseFragment
import com.quannm18.coolmateapp.view.adapter.BillAdapter
import kotlinx.android.synthetic.main.fragment_shiping_now.*

class ShippingNowFragment : BaseFragment(){
    private val billAdapter: BillAdapter = BillAdapter()
    override fun layoutID(): Int = R.layout.fragment_shiping_now

    override fun initData() {

    }

    override fun initView() {
        rcvShippingNow.apply {
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