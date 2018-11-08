package com.example.asus.medihome.ui.mediclinic.profile

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.asus.medihome.R
import com.example.asus.medihome.ui.booking_kamar.profile_rs.InformasiFragment
import com.example.asus.medihome.ui.booking_kamar.profile_rs.KamarFragment

class SectionPagerAdapter(fm: FragmentManager, var mContext : Context) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> InformasiKlinikFragment()
            1 -> LayananKlinikFragment()
            else -> KamarFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> mContext.getString(R.string.informasi)
            1 -> mContext.getString(R.string.layanan)
            else -> null
        }
    }
}