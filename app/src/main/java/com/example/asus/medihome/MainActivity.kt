package com.example.asus.medihome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.asus.medihome.extension.toast
import com.example.asus.medihome.notification.MyFirebaseMessagingService
import com.example.asus.medihome.ui.emergency.EmergencyFragment
import com.example.asus.medihome.ui.home.HomeFragment
import com.example.asus.medihome.ui.info_sehat.InfoSehatFragment
import com.example.asus.medihome.ui.pesanan.PesananFragment
import com.example.asus.medihome.ui.profil.ProfilFragment
import com.example.asus.medihome.util.PreferenceHelper
import com.example.asus.medihome.util.PreferenceHelper.get
import com.example.asus.medihome.util.PreferenceHelper.set
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateFragment(HomeFragment())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        sendTokenToServer()

    }

    private fun sendTokenToServer() {
        Log.d("MainActivity", "send token to server")
        val prefs = PreferenceHelper.defaultPrefs(this)

        if (!prefs["tokenSended", false]!!) {
            val token: String? = prefs["firebaseToken"]
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            val userRef = FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("users")
                    .child(userId!!)


            userRef.child("token").setValue(token).addOnCompleteListener {
                if (it.isSuccessful) {
                    prefs["tokenSended"] = true
                }
            }

        }

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportActionBar?.title = "HaloMedis"
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


    private fun updateFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit()
    }

    private var exit: Boolean? = false
    override fun onBackPressed() {
        if (exit!!) {
            finishAffinity() // finish all parent activities
        } else {
            toast("Press Back again to Exit")
            exit = true
            Handler().postDelayed({ exit = false }, (3 * 1000).toLong())
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


}
