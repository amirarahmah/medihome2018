package com.example.asus.medihome.ui.booking_kamar

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.asus.medihome.R
import com.example.asus.medihome.util.setupView
import kotlinx.android.synthetic.main.activity_cari_rumah_sakit.*
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import com.example.asus.medihome.ui.booking_kamar.dialog.CariLokasiDialog


class CariRumahSakitActivity : AppCompatActivity(), CariLokasiDialog.OnLokasiClicked {


    private var modeCari : String = "Lokasi"
    private var lokasi : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari_rumah_sakit)

        supportActionBar?.title = "Cari Rumah Sakit"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val listCari = arrayListOf("Lokasi", "Nama Rumah Sakit")
        spinner_cari_berdasarkan.setupView(listCari)

        spinner_cari_berdasarkan.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                modeCari = parent?.getItemAtPosition(position).toString()
                if(modeCari == listCari[1]){
                    container_cari_lokasi.visibility = View.GONE
                    container_cari_namars.visibility = View.VISIBLE
                }else{
                    container_cari_namars.visibility = View.GONE
                    container_cari_lokasi.visibility = View.VISIBLE
                }
            }

        }


        btn_cari.setOnClickListener {
            when {
                modeCari.equals("nama lokasi") -> {
                    val intent = Intent(this, HospitalListActivity::class.java)
                    intent.putExtra("modeCari", "lokasi")
                    intent.putExtra("kota", lokasi)
                    startActivity(intent)
                }
                modeCari == listCari[0] -> {
                    val intent = Intent(this, NearbyHospitalActivity::class.java)
                    startActivity(intent)
                }
                modeCari == listCari[1] -> {
                    val namaRs = nama_rumah_sakit.text.toString().trim()
                    val intent = Intent(this, HospitalListActivity::class.java)
                    intent.putExtra("modeCari", "nama")
                    intent.putExtra("namaRs", namaRs)
                    startActivity(intent)
                }
            }

        }


        lokasi_rumah_sakit.setOnClickListener {
            showLokasiDialog()
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


    override fun sendLokasi(lokasi: String) {
        if(lokasi.equals("nearby")){
            modeCari = "Lokasi"
            lokasi_rumah_sakit.text = "Rumah Sakit sekitar Anda"
        }else{
            this.lokasi = lokasi
            modeCari = "nama lokasi"
            lokasi_rumah_sakit.text = lokasi
        }

    }

    private fun showLokasiDialog() {
        val lokasiDialog = CariLokasiDialog()
        lokasiDialog.show(supportFragmentManager,
                "lokasi_dialog_fragment")
    }


    private fun showListTindakanDialog() {
        val alertDialog = AlertDialog.Builder(this)
        val tindakanList = arrayOf("Bebas", "Kemoteraphy", "CT Scan",
                "Radiologi", "Bedah Plastik")
        alertDialog.setItems(tindakanList) { dialog, position ->
            tindakan_tv.text = tindakanList[position]
            dialog.dismiss()
        }

        alertDialog.create().show()
    }


    private fun showListSpesialisasiDialog() {
        val alertDialog = AlertDialog.Builder(this)
        val tindakanList = arrayOf("Bebas", "Dokter Gigi", "Dokter Mata", "Psikiater",
                "Radiologi", "Saraf", "THT")
        alertDialog.setItems(tindakanList) { dialog, position ->
            spesialisasi_tv.text = tindakanList[position]
            dialog.dismiss()
        }

        alertDialog.create().show()
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
