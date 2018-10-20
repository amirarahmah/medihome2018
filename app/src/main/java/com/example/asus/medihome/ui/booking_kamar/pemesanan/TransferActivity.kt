package com.example.asus.medihome.ui.booking_kamar.pemesanan

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.asus.medihome.MainActivity
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Pesanan
import com.example.asus.medihome.util.PreferenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_transfer.*

class TransferActivity : AppCompatActivity() {

    private lateinit var prefs : SharedPreferences
    private var cTimer : CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        supportActionBar?.title = "Transfer"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefs = PreferenceHelper.defaultPrefs(this)

        val totalPembayaran = prefs.getInt("totalPembayaran", 0)

        total_pembayaran.text = "Rp $totalPembayaran"

        button_done.setOnClickListener {
            saveOrderTodatabase()
        }


        startTimer()


    }

    private fun startTimer() {
        cTimer = object : CountDownTimer(3540 * 1000+1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000)
                val minutes = seconds / 60
                seconds %= 60

                waktu_pembayaran.text = String.format("%d:%02d", minutes, seconds);

            }
            override fun onFinish() {}
        }
        cTimer?.start()
    }

    private fun saveOrderTodatabase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val idPesanan = intent?.extras?.getString("idPesanan")
        val namaRumahSakit = prefs.getString("hospitalName", "")
        val tanggalPesan = prefs.getString("tanggalPesan", "")
        val tipeKamar = prefs.getString("jenisKamar", "")
        val namaPasien = prefs.getString("namaPasien", "")

        val pesananRef = FirebaseDatabase.getInstance().reference.child("pesanan")

        val pesanan = Pesanan(idPesanan, namaRumahSakit, tipeKamar, tanggalPesan, namaPasien)

        pesananRef.child(userId!!).child(idPesanan!!).setValue(pesanan).addOnCompleteListener {task ->
            if(task.isSuccessful){
                navigateToMainActivity()
            }else{
                Toast.makeText(this, ""+task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                showAlertDialog()
                return true
            }
        }
        return false
    }

    private fun showAlertDialog() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Anda yakin ingin meninggalkan halaman ini?")
                .setMessage("Anda akan diarahkan ke halaman utama")
                .setPositiveButton("Ya") { dialog, which ->
                    navigateToMainActivity()
                }
                .setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }

        val alertDialog = alertBuilder.create()
        alertDialog.show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
