package com.example.asus.medihome.ui.booking_kamar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asus.medihome.R

class CariRumahSakitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari_rumah_sakit)

        supportActionBar?.title = "Cari Rumah Sakit"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


}
