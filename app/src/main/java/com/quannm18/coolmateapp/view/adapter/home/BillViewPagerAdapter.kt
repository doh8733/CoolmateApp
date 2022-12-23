package com.quannm18.coolmateapp.view.adapter.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.quannm18.coolmateapp.view.activity.BillHomeActivity
import com.quannm18.coolmateapp.view.activity.HomeActivity
import com.quannm18.coolmateapp.view.fragment.GiamGiaFragment
import com.quannm18.coolmateapp.view.fragment.HotFragment
import com.quannm18.coolmateapp.view.fragment.KhamPhaFragment
import com.quannm18.coolmateapp.view.fragment.NoiBatFragment
import com.quannm18.coolmateapp.view.fragment.bill.CancelBillFragment
import com.quannm18.coolmateapp.view.fragment.bill.PendingFragment
import com.quannm18.coolmateapp.view.fragment.bill.ShippingNowFragment
import com.quannm18.coolmateapp.view.fragment.bill.WasShippingFragment

class BillViewPagerAdapter(fragment: BillHomeActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                PendingFragment()
            }
            1 -> {
                ShippingNowFragment()
            }
            2 -> {
                WasShippingFragment()
            }
            3 -> {
                CancelBillFragment()
            }
            else -> {
                PendingFragment()
            }
        }
    }
}