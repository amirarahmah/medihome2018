package com.example.asus.medihome.ui.home


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome.MainActivity

import com.example.asus.medihome.R
import com.example.asus.medihome.ui.booking_kamar.CariRumahSakitActivity
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val images = arrayOf(R.drawable.ads, R.drawable.iklan, R.drawable.iklan)

        val viewPagerAdapter = ViewPagerAdapter(context, images)
        viewPager.adapter = viewPagerAdapter
        indicator.setViewPager(viewPager)

        menu_booking_kamar.setOnClickListener {
            val intent = Intent(context, CariRumahSakitActivity::class.java)
            startActivity(intent)
        }

    }





}
