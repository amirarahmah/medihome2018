package com.example.asus.medihome.ui.booking_kamar.pemesanan

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.asus.medihome.R
import com.example.asus.medihome.util.PreferenceHelper
import kotlinx.android.synthetic.main.activity_pemesanan1.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class Pemesanan1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemesanan1)

        supportActionBar?.title = "Isi Data"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = PreferenceHelper.defaultPrefs(this)

        val namaPemesan = prefs.getString("nama", "")
        val emailPemesan = prefs.getString("email", "")
        val noTelp = prefs.getString("noTelp", "")

        nama_lengkap_pemesan.text = "Nama Lengkap : $namaPemesan"
        email_pemesan.text = "Alamat Email : $emailPemesan"
        no_telepon_pemesan.text = "Nomor Telepon : $noTelp"

        val hospitalName = intent?.extras?.getString("hospitalName")
        val jenisKamar = intent?.extras?.getString("jenisKamar")
        nama_hospital.text = hospitalName
        jenis_kamar.text = jenisKamar

        val date = Calendar.getInstance().time
        val dateFormater = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val formatedDate = dateFormater.format(date)


        tanggal_pesan.text = formatedDate

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
