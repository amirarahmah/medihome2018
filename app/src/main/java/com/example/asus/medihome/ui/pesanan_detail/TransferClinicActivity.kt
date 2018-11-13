package com.example.asus.medihome.ui.pesanan_detail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.asus.medihome.R
import kotlinx.android.synthetic.main.activity_transfer_clinic.*

class TransferClinicActivity : AppCompatActivity() {

    private var cTimer : CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_clinic)

        supportActionBar?.title = "Transfer"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val totalPembayaran = intent?.extras?.getString("hargaLayanan")

        total_pembayaran.text = "$totalPembayaran"

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

}
