package com.quannm18.coolmateapp.view.adapter.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.quannm18.coolmateapp.view.activity.HomeActivity
import com.quannm18.coolmateapp.view.fragment.GiamGiaFragment
import com.quannm18.coolmateapp.view.fragment.HotFragment
import com.quannm18.coolmateapp.view.fragment.KhamPhaFragment
import com.quannm18.coolmateapp.view.fragment.NoiBatFragment

class HomeViewPagerAdapter(fragment: HomeActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                NoiBatFragment()
            }
            1 -> {
                HotFragment()
            }
            2 -> {
                GiamGiaFragment()
            }
            3 -> {
                KhamPhaFragment()
            }
            else -> {
                GiamGiaFragment()
            }
        }
    }
}