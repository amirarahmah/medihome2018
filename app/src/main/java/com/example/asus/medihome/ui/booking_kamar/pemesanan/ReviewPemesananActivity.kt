package com.example.asus.medihome.ui.booking_kamar.pemesanan

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.asus.medihome.R
import com.example.asus.medihome.util.PreferenceHelper
import com.example.asus.medihome.util.PreferenceHelper.set
import kotlinx.android.synthetic.main.activity_pemesanan2.*
import java.util.*

class ReviewPemesananActivity : AppCompatActivity() {

    private lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemesanan2)

        supportActionBar?.title = "Review Pesanan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefs = PreferenceHelper.defaultPrefs(this)

        val namaRumahSakit = prefs.getString("hospitalName", "")
        val tanggalPesan = prefs.getString("tanggalPesan", "")
        val tipeKamar = prefs.getString("jenisKamar", "")
        val namaPasien = prefs.getString("namaPasien", "")
        val tglLahir = prefs.getString("tanggalLahirPasien", "")
        val hargaKamar = prefs.getString("hargaKamar", "")

        nama_hospital.text = "Rumah Sakit : $namaRumahSakit"
        tanggal_pesan.text = "Check In : $tanggalPesan"
        jenis_kamar.text = "Tipe Kamar : $tipeKamar"
        nama_pasien.text = "Nama Pasien : $namaPasien"
        tanggal_lahir_pasien.text = "Tanggal Lahir : $tglLahir"

        harga_kamar.text = "Rp $hargaKamar"

        val kodeUnik = Random(). nextInt(900) + 100
        kode_unik.text = "Rp $kodeUnik"

        val hargaKamarInt = hargaKamar?.replace(".", "")?.toInt()
        val totalPembayaran = hargaKamarInt!! + kodeUnik
        total_pembayaran.text = "Rp $totalPembayaran"

        button_next.setOnClickListener {
            prefs["totalPembayaran"] = totalPembayaran
            val intent = Intent(this@ReviewPemesananActivity, MetodePembayaranActivity::class.java)
            startActivity(intent)
        }

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
