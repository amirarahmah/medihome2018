package com.example.asus.medihome.ui.mediclinic.reservasi

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.asus.medihome.R
import com.example.asus.medihome.ui.booking_kamar.pemesanan.dialog.DataPasien2Dialog
import com.example.asus.medihome.ui.booking_kamar.pemesanan.dialog.DataPemesanDialog
import com.example.asus.medihome.util.PreferenceHelper
import kotlinx.android.synthetic.main.activity_reservasi.*

class ReservasiActivity : AppCompatActivity(), DataPemesanDialog.OnDataSaved {

    private lateinit var prefs : SharedPreferences

    var namaPemesan: String = ""
    var emailPemesan: String = ""
    var noTelp: String = ""

    override fun sendDataPemesan(namaLengkap: String, email: String, phone: String) {
        namaPemesan = namaLengkap
        emailPemesan = email
        noTelp = phone

        setDataPemesan()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservasi)

        supportActionBar?.title = "Reservasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val namaKlinik = intent?.extras?.getString("namaKlinik")
        val namaLayanan = intent?.extras?.getString("namaLayanan")

        nama_klinik.text = namaKlinik
        nama_layanan.text = namaLayanan

        prefs = PreferenceHelper.defaultPrefs(this)

        namaPemesan = prefs.getString("nama", "")
        emailPemesan = prefs.getString("email", "")
        noTelp = prefs.getString("noTelp", "")

        setDataPemesan()


        tv_ubah_pemesan.setOnClickListener {
            showDataPemesanDialog()
        }

    }


    private fun showDataPasienDialog() {
        val dataPemesanDialog = DataPasien2Dialog()
        dataPemesanDialog.show(supportFragmentManager,
                "edit_pasien_dialog_fragment")
    }

    private fun showDataPemesanDialog() {
        val dataPemesanDialog = DataPemesanDialog()
        dataPemesanDialog.show(supportFragmentManager,
                "edit_pemesan_dialog_fragment")
    }


    private fun setDataPemesan() {
        nama_lengkap_pemesan.text = "Nama Lengkap : $namaPemesan"
        email_pemesan.text = "Alamat Email : $emailPemesan"
        no_telepon_pemesan.text = "Nomor Telepon : $noTelp"
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
