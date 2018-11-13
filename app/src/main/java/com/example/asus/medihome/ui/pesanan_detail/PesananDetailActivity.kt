package com.example.asus.medihome.ui.pesanan_detail

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Reservation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pesanan_detail.*

class PesananDetailActivity : AppCompatActivity() {

    var idReservation : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan_detail)

        idReservation = intent?.extras?.getString("idReservation")

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
            navigateToReviewPesanan()
        }

    }

    private fun navigateToReviewPesanan() {
        val intent = Intent(this, ReviewPesananClinicActivity::class.java)
        intent.putExtra("idReservation", idReservation)
        startActivity(intent)
    }

    private fun updateUI(reservation: Reservation?) {

        supportActionBar?.title = reservation?.namaKlinik
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nama_klinik.text = reservation?.namaKlinik
        alamat_klinik.text = reservation?.alamatKlinik
        nama_layanan.text = reservation?.layanan
        nama_dokter.text = reservation?.dokter
        harga_layanan.text = reservation?.harga
        id_reservasi_tv.text = reservation?.idReservation
        tanggal_tv.text = reservation?.tanggal
        pukul_tv.text = reservation?.pukul
        nama_pasien_tv.text = reservation?.namaPasien
        tanggal_lahir_tv.text = reservation?.tanggalLahir
        jenis_kelamin_tv.text = reservation?.jenisKelamin
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
