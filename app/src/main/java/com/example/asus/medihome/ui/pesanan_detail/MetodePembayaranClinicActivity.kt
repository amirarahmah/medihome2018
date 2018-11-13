package com.example.asus.medihome.ui.pesanan_detail

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.asus.medihome.R
import kotlinx.android.synthetic.main.activity_metode_pembayaran.*

class MetodePembayaranClinicActivity : AppCompatActivity() {

    private var hargaLayanan: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metode_pembayaran)

        supportActionBar?.title = "Metode Pembayaran"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        id_pesanan_tv.text = "ID Reservasi : "

        val idReservation = intent?.extras?.getString("idReservasi")
        hargaLayanan = intent?.extras?.getString("hargaLayanan")

        id_pesanan.text = idReservation
        jumlah_bayar.text = hargaLayanan

        button_next.setOnClickListener {
            navigateToTransferActivity()
        }

    }

    private fun navigateToTransferActivity() {
        val intent = Intent(this, TransferClinicActivity::class.java)
        intent.putExtra("hargaLayanan", hargaLayanan)
        startActivity(intent)
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
