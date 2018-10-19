package com.example.asus.medihome.ui.booking_kamar.profile_rs

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.asus.medihome.R
import kotlinx.android.synthetic.main.activity_profil_rumah_sakit.*

class ProfilRumahSakitActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var hospitalName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_rumah_sakit)

        hospitalName = intent?.extras?.getString("nama")

        collapse_toolbar.title = hospitalName

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        setProfileRumahSakit()

        btn_pesan_kamar.setOnClickListener {
            tabs.getTabAt(1)?.select()
        }

    }

    private fun setProfileRumahSakit() {
        val hospitalId = intent?.extras?.getString("hospitalId")
        val hospitalAlamat = intent?.extras?.getString("alamat")
        val hospitalPhone = intent?.extras?.getString("nomorTelpon")
        val photo = intent?.extras?.getString("photo")

        Glide.with(this)
                .load(photo)
                .into(image_rumah_sakit)

        hospital_name.text = hospitalName
        hospital_location.text = hospitalAlamat


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }

}
