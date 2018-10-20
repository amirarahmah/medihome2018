package com.example.asus.medihome.ui.booking_kamar.profile_rs

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.asus.medihome.R
import kotlinx.android.synthetic.main.activity_profil_rumah_sakit.view.*

class SectionsPagerAdapter(fm: FragmentManager, var mContext : Context) : FragmentPagerAdapter(fm) {

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


    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> mContext.getString(R.string.informasi)
            1 -> mContext.getString(R.string.kamar)
            else -> null
        }
    }
}