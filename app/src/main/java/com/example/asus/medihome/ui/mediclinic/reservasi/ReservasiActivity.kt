package com.example.asus.medihome.ui.mediclinic.reservasi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.asus.medihome.MainActivity
import com.example.asus.medihome.R
import com.example.asus.medihome.R.string.layanan
import com.example.asus.medihome.model.Reservation
import com.example.asus.medihome.ui.booking_kamar.pemesanan.dialog.DataPasien2Dialog
import com.example.asus.medihome.ui.booking_kamar.pemesanan.dialog.DataPemesanDialog
import com.example.asus.medihome.util.PreferenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_reservasi.*
import java.security.SecureRandom

class ReservasiActivity : AppCompatActivity(), DataPemesanDialog.OnDataSaved,
        DataPasien2Dialog.OnDataPasienSaved, PilihJadwalDialog.OnJadwalSaved {


    private lateinit var prefs: SharedPreferences

    var namaKlinik : String? = ""
    var alamatKlinik : String? = ""
    var namaLayanan : String? = ""
    var hargaLayanan : String? = ""

    var namaPemesan: String = ""
    var emailPemesan: String = ""
    var noTelp: String = ""

    var namaDokter : String = ""
    var tanggalReservasi : String = ""
    var pukul : String = ""

    var namaPasien : String = ""
    var jenisKelaminPasien : String = ""
    var tglLahirPasien : String = ""

    override fun sendDataPemesan(namaLengkap: String, email: String, phone: String) {
        namaPemesan = namaLengkap
        emailPemesan = email
        noTelp = phone

        setDataPemesan()
    }


    override fun sendJadwal(dokter: String, tanggal: String, jadwal: String) {
        namaDokter = dokter
        tanggalReservasi = tanggal
        pukul = jadwal
        nama_dokter.text = "Nama Dokter : "+dokter
        tanggal_tv.text = "Tanggal : "+tanggal
        pukul_tv.text = "Pukul : "+jadwal
    }


    override fun sendDataPasien(namaPasien: String, tanggalLahirPasien: String,
                                jenisKelaminPasien: String, email: String,
                                phone: String, noRekam: String) {

        nama_lengkap_pasien.text = "Nama Lengkap : $namaPasien"
        tanggal_lahir_pasien.text = "Tanggal Lahir : $tanggalLahirPasien"
        kelamin_pasien.text = "Kelamin : $jenisKelaminPasien"
        email_pasien.text = "Email : $email"
        no_telepon_pasien.text = "No. telepon : $phone"
        no_rekam_pasien.text = "No Rekam Medis : $noRekam"

        this.namaPasien = namaPasien
        this.jenisKelaminPasien = jenisKelaminPasien
        this.tglLahirPasien = tanggalLahirPasien

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservasi)

        supportActionBar?.title = "Reservasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        namaKlinik = intent?.extras?.getString("namaKlinik")
        alamatKlinik = intent?.extras?.getString("alamatKlinik")
        namaLayanan = intent?.extras?.getString("namaLayanan")
        hargaLayanan = intent?.extras?.getString("hargaLayanan")

        nama_klinik.text = namaKlinik
        nama_layanan.text = namaLayanan
        harga_layanan.text = hargaLayanan

        prefs = PreferenceHelper.defaultPrefs(this)

        namaPemesan = prefs.getString("nama", "")
        emailPemesan = prefs.getString("email", "")
        noTelp = prefs.getString("noTelp", "")

        setDataPemesan()


        tv_ubah_pemesan.setOnClickListener {
            showDataPemesanDialog()
        }

        tv_ubah_pasien.setOnClickListener {
            showDataPasienDialog()
        }

        tv_ubah_jadwal.setOnClickListener {
            showPilihJadwalDialog()
        }

        button_reservasi.setOnClickListener {
            saveReservationToDatabase()
        }

    }

    private fun saveReservationToDatabase() {
        val reservasiRef = FirebaseDatabase.getInstance().reference.child("reservasi")

        val random = SecureRandom()
        val sb = StringBuilder(14)
        val ab = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

        for (i in 1..14) {
            sb.append(ab[random.nextInt(ab.length)])
        }

        val idReservasi = sb.toString()

        val idKlinik = intent?.extras?.getString("idKlinik")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val reservasi = Reservation(idReservasi, idKlinik!!, userId!!, namaKlinik!!, alamatKlinik!!,
                namaLayanan!!, namaDokter, hargaLayanan!!, tanggalReservasi, pukul, namaPasien,
                jenisKelaminPasien, tglLahirPasien)


        reservasiRef.child(idReservasi).setValue(reservasi).addOnCompleteListener {task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Reservasi berhasil dilakukan", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            }else{
                Toast.makeText(this, ""+task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showPilihJadwalDialog() {
        val pilihJadwalDialog = PilihJadwalDialog()
        pilihJadwalDialog.show(supportFragmentManager,
                "pilih_jadwal_dialog_fragment")
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


    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
