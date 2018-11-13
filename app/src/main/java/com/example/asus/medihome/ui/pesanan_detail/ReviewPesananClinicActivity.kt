package com.example.asus.medihome.ui.pesanan_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Reservation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_review_pesanan_clinic.*

class ReviewPesananClinicActivity : AppCompatActivity() {

    private var harga : String? = ""
    private var idReservasi : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_pesanan_clinic)

        supportActionBar?.title = "Review Pesanan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val idReservation = intent?.extras?.getString("idReservation")

        val reservasinRef = FirebaseDatabase.getInstance().reference.child("reservasi")

        reservasinRef.child(idReservation!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val reservation = p0.getValue(Reservation::class.java)
                        updateUI(reservation)
                    }
                })

        button_next.setOnClickListener {
            navigateToMetodePembayaran()
        }

    }

    private fun navigateToMetodePembayaran() {
        val intent = Intent(this, MetodePembayaranClinicActivity::class.java)
        intent.putExtra("hargaLayanan", harga)
        intent.putExtra("idReservasi", idReservasi)
        startActivity(intent)
    }


    @SuppressLint("SetTextI18n")
    private fun updateUI(reservation: Reservation?) {
        idReservasi = reservation?.idReservation

        id_reservasi.text = "ID Reservasi : ${reservation?.idReservation}"
        nama_klinik.text = "Klinik : ${reservation?.namaKlinik}"
        nama_layanan.text = "Layanan : ${reservation?.layanan}"
        nama_dokter.text = "Dokter : ${reservation?.dokter}"
        tanggal.text = "Tanggal : ${reservation?.tanggal}"
        pukul.text = "Pukul : ${reservation?.pukul}"
        nama_pasien.text = "Nama Pasien : ${reservation?.namaPasien}"

        harga = reservation?.harga
        harga_layanan.text = harga
        total_pembayaran.text = harga
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
