package com.example.asus.medihome.ui.mediclinic.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.example.asus.medihome.R
import com.example.asus.medihome.ui.booking_kamar.profile_rs.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_profile_clinic.*

class ProfileClinicActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionPagerAdapter? = null
    private var clinicName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_clinic)


        clinicName = intent?.extras?.getString("nama")

        collapse_toolbar.title = clinicName

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        mSectionsPagerAdapter = SectionPagerAdapter(supportFragmentManager, this)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

        tabs.setupWithViewPager(container)
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val tabPosition = tab.position
                if(tabPosition == 0){
                    btn_pesan_kamar.visibility = View.VISIBLE
                }else{
                    btn_pesan_kamar.visibility = View.GONE
                }
            }

        })

        setProfileRumahSakit()

        btn_pesan_kamar.setOnClickListener {
            tabs.getTabAt(1)?.select()
        }

    }

    private fun setProfileRumahSakit() {
        val photo = intent?.extras?.getString("photo")
        val kategori = intent?.extras?.getString("kategoriKlinik")

        Glide.with(this)
                .load(photo)
                .into(image_klinik)

        nama_klinik.text = clinicName
        kategori_klinik.text = kategori

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
