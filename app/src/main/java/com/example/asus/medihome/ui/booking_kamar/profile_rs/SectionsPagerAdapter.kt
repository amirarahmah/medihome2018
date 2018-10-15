package com.example.asus.medihome.ui.booking_kamar.profile_rs

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> InformasiFragment()
            1 -> KamarFragment()
            else -> KamarFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}