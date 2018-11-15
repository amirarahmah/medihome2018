package com.example.asus.medihome.ui.pesanan_detail

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.asus.medihome.MainActivity
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Reservation
import com.example.asus.medihome.ui.authentification.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_pesanan_detail.*

class PesananDetailActivity : AppCompatActivity() {

    var idReservation : String? = ""
    lateinit var reservasinRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan_detail)

        idReservation = intent?.extras?.getString("idReservation")

        reservasinRef = FirebaseDatabase.getInstance().reference.child("reservasi")

        reservasinRef.child(idReservation!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if(p0.exists()){
                            val reservation = p0.getValue(Reservation::class.java)
                            updateUI(reservation)
                        }
                    }
                })

        button_next.setOnClickListener {
            navigateToReviewPesanan()
        }

        btn_batal_reservasi.setOnClickListener {
           showdialog()
        }

    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun navigateToReviewPesanan() {
        val intent = Intent(this, ReviewPesananClinicActivity::class.java)
        intent.putExtra("idReservation", idReservation)
        startActivity(intent)
    }

    private fun updateUI(reservation: Reservation?) {

        supportActionBar?.title = reservation?.namaKlinik
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(reservation?.confirmed == ""){
            button_next.visibility = View.GONE
        }else if(reservation?.confirmed == "true"){
            info_pesanan.text = "Selesaikan pembayaran dalam waktu:\n" +
                    "59:00"
            button_next.visibility = View.VISIBLE
        }else if(reservation?.confirmed == "false"){
            info_pesanan.text = "Reservasi yang Anda lakukan ditolak."
            info_penolakan_container.visibility = View.VISIBLE
            informasi_penolakan.text = reservation.alasan
            detail_pesanan.visibility = View.GONE
            btn_batal_reservasi.visibility = View.GONE
            button_next.visibility = View.GONE
        }

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


    private fun showdialog() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage("Apakah Anda yakin untuk membatalkan reservasi?")
                .setPositiveButton("Ya") { _, _ ->
                    batalkanReservasi()
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }

        val alertDialog = alertBuilder.create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }
        alertDialog.show()

    }

    private fun batalkanReservasi() {
        reservasinRef.child(idReservation!!).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this@PesananDetailActivity, "Reservasi berhasil dibatalkan",
                        Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            }
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
