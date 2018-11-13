package com.example.asus.medihome.ui.pesanan

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.asus.medihome.R
import com.example.asus.medihome.ui.booking_kamar.profile_rs.InformasiFragment
import com.example.asus.medihome.ui.booking_kamar.profile_rs.KamarFragment

class PesananPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> PesananHospitalFragment()
            1 -> PesananClinicFragment()
            else -> PesananHospitalFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "MediHospital"
            1 -> "MediClinic"
            else -> null
        }
    }
}