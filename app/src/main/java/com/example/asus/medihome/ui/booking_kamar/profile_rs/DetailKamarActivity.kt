package com.example.asus.medihome.ui.booking_kamar.profile_rs

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.asus.medihome.R
import com.example.asus.medihome.R.string.kamar
import com.example.asus.medihome.model.Fasilitas
import com.example.asus.medihome.ui.booking_kamar.pemesanan.IsiDataActivity
import com.example.asus.medihome.ui.booking_kamar.profile_rs.adapter.FasilitasAdapter
import kotlinx.android.synthetic.main.activity_detail_kamar.*

class DetailKamarActivity : AppCompatActivity() {

    private lateinit var mAdapter : FasilitasAdapter
    private lateinit var mFasilitasList : ArrayList<Fasilitas>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kamar)

        supportActionBar?.title = "Detail Kamar"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mFasilitasList = arrayListOf(Fasilitas("1", "AC", R.drawable.medihome_aircond_icon),
                Fasilitas("2", "Lift", R.drawable.medihome_elevator_icon),
                Fasilitas("3", "Parkir", R.drawable.medihome_parking_icon),
                Fasilitas("4", "P3K", R.drawable.medihome_firstaid_icon))

        setupRecyclerView()

        btn_pesan_kamar.setOnClickListener {
            onPesanKamarClicked()
        }

    }

    private fun onPesanKamarClicked() {
        val hospitalName = intent?.extras?.getString("hospitalName")
        val jenisKamar = intent?.extras?.getString("jenisKamar")
        val hargaKamar = intent?.extras?.getString("hargaKamar")
        val intent = Intent(this, IsiDataActivity::class.java)
        intent.putExtra("hospitalName", hospitalName)
        intent.putExtra("jenisKamar", jenisKamar)
        intent.putExtra("hargaKamar", hargaKamar)
        startActivity(intent)
    }


    private fun setupRecyclerView() {
        fasilitas_recyclerView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)
        mAdapter = FasilitasAdapter(mFasilitasList)
        fasilitas_recyclerView.adapter = mAdapter
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
