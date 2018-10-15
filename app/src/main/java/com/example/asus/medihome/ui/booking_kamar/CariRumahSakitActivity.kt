package com.example.asus.medihome.ui.booking_kamar

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.asus.medihome.R
import com.example.asus.medihome.util.setupView
import kotlinx.android.synthetic.main.activity_cari_rumah_sakit.*
import android.widget.Toast
import android.content.DialogInterface
import android.R.array
import android.support.v7.app.AlertDialog


class CariRumahSakitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari_rumah_sakit)

        supportActionBar?.title = "Cari Rumah Sakit"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val listCari = arrayListOf("Lokasi", "Nama RS")
        spinner_cari_berdasarkan.setupView(listCari)

        btn_cari.setOnClickListener {
            val intent = Intent(this, NearbyHospitalActivity::class.java)
            startActivity(intent)
        }

        tindakan_tv.setOnClickListener {
            showListTindakanDialog()
        }

        spesialisasi_tv.setOnClickListener {
            showListSpesialisasiDialog()
        }

        fasilitas_tv.setOnClickListener {
            showListFasilitasDialog()
        }

    }

    private fun showListTindakanDialog() {
        val dialog = AlertDialog.Builder(this)
        val tindakanList = arrayOf("Bebas", "Kemoteraphy", "CT Scan",
                "Radiologi", "Bedah Plastik")
        dialog.setItems(tindakanList) { dialog, position ->
            tindakan_tv.text = tindakanList[position]
            dialog.dismiss()
        }

        dialog.create().show()
    }


    private fun showListSpesialisasiDialog() {
        val dialog = AlertDialog.Builder(this)
        val tindakanList = arrayOf("Bebas", "Dokter Gigi", "Dokter Mata", "Psikiater",
                "Radiologi", "Saraf", "THT")
        dialog.setItems(tindakanList) { dialog, position ->
            spesialisasi_tv.text = tindakanList[position]
            dialog.dismiss()
        }

        dialog.create().show()
    }


    private fun showListFasilitasDialog() {
        val dialog = AlertDialog.Builder(this)
        val tindakanList = arrayOf("Bebas", "Wifi", "Parkir", "ATM", "Kantin", "Cafe")
        dialog.setItems(tindakanList) { dialog, position ->
            fasilitas_tv.text = tindakanList[position]
            dialog.dismiss()
        }

        dialog.create().show()
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
