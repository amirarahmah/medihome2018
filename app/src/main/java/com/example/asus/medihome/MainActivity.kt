package com.example.asus.medihome

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.asus.medihome.ui.emergency.AmbulansActivity
import com.example.asus.medihome.ui.emergency.EmergencyFragment
import com.example.asus.medihome.ui.home.HomeFragment
import com.example.asus.medihome.ui.info_sehat.InfoSehatFragment
import com.example.asus.medihome.ui.pesanan.PesananFragment
import com.example.asus.medihome.ui.profil.ProfilFragment
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
                supportActionBar?.title = "MediHome"
                updateFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_info_sehat -> {
                supportActionBar?.title = "Info Sehat"
                updateFragment(InfoSehatFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_emergency -> {
                supportActionBar?.title = "Ambulans Now"
//                window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.red)
//                supportActionBar?.setBackgroundDrawable(ColorDrawable(
//                        ContextCompat.getColor(this, R.color.red)))
                updateFragment(EmergencyFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_pesanan -> {
                supportActionBar?.title = "Pesanan Saya"
                updateFragment(PesananFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                supportActionBar?.title = "Profil Saya"
                updateFragment(ProfilFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun navigateToAmbulansActivity() {
        val intent = Intent(this, AmbulansActivity::class.java)
        startActivity(intent)
    }


    private fun updateFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit()
    }

}
