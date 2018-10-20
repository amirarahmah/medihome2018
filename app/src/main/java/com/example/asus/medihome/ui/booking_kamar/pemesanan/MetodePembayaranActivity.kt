package com.example.asus.medihome.ui.booking_kamar.pemesanan

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asus.medihome.R
import kotlinx.android.synthetic.main.activity_metode_pembayaran.*
import java.security.SecureRandom
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.view.MenuItem
import com.example.asus.medihome.util.PreferenceHelper


class MetodePembayaranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metode_pembayaran)

        supportActionBar?.title = "Metode Pembayaran"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val random = SecureRandom()
        val sb = StringBuilder(12)
        val ab = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

        for (i in 1..12) {
            sb.append(ab[random.nextInt(ab.length)])
        }

        val idPesanan = sb.toString()

        id_pesanan.text = idPesanan

        val prefs = PreferenceHelper.defaultPrefs(this)

        val jenisKamar = prefs.getString("jenisKamar", "")
        val namaRs = prefs.getString("hospitalName", "")

        button_next.setOnClickListener {
            showNotification(this, "Pemesanan Kamar",
                    "Silakan melakukan pembayaran kamar $jenisKamar $namaRs")
            val intent = Intent(this@MetodePembayaranActivity, TransferActivity::class.java)
            intent.putExtra("idPesanan", idPesanan)
            startActivity(intent)

        }
    }


    fun showNotification(context: Context, title: String, body: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 1
        val channelId = "channel-01"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                    channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        val mBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)

        notificationManager.notify(notificationId, mBuilder.build())
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
