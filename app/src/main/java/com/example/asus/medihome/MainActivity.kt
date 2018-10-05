package com.example.asus.medihome

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.asus.medihome.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateFragment(HomeFragment())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                updateFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_info_sehat -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_emergency -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_pesanan -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private fun updateFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit()
    }

}
