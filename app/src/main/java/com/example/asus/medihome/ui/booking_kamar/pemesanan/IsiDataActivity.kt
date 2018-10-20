package com.example.asus.medihome.ui.booking_kamar.pemesanan

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.asus.medihome.R
import com.example.asus.medihome.ui.booking_kamar.pemesanan.dialog.DataPasienDialog
import com.example.asus.medihome.ui.booking_kamar.pemesanan.dialog.DataPemesanDialog
import com.example.asus.medihome.util.PreferenceHelper
import com.example.asus.medihome.util.PreferenceHelper.set
import kotlinx.android.synthetic.main.activity_isi_data.*
import java.text.SimpleDateFormat
import java.util.*

class IsiDataActivity : AppCompatActivity(), DataPemesanDialog.OnDataSaved,
        DataPasienDialog.OnDataPasienSaved {

    var namaPemesan: String = ""
    var emailPemesan: String = ""
    var noTelp: String = ""

    var hospitalName: String? = ""
    var jenisKamar: String? = ""
    var hargaKamar: String? = ""

    var namaPasien: String = ""

    private var datePickerDialog: DatePickerDialog? = null
    private var dateFormatter: SimpleDateFormat? = null

    private lateinit var prefs : SharedPreferences

    private var tanggalPesan : String = ""

    override fun sendDataPemesan(namaLengkap: String, email: String, phone: String) {
        namaPemesan = namaLengkap
        emailPemesan = email
        noTelp = phone

        setDataPemesan()
    }

    override fun sendDataPasien(namaPasien: String, tanggalLahirPasien: String,
                                tempatLahirPasien: String, jenisKelaminPasien: String,
                                satusPerkawinan: String, alamatPasien: String,
                                pekerjaanPasien: String, goldarPasien: String) {

        this.namaPasien = namaPasien
        nama_lengkap_pasien.text = "Nama Lengkap : $namaPasien"
        tanggal_lahir_pasien.text = "Tanggal Lahir : $tanggalLahirPasien"
        tempat_lahir_pasien.text = "Tempat Lahir : $tempatLahirPasien"
        kelamin_pasien.text = "Kelamin : $jenisKelaminPasien"
        status_pasien.text = "Status Perkawinan : $satusPerkawinan"
        alamat_domisili_pasien.text = "Alamat Domisili : $alamatPasien"
        pekerjaan_pasien.text = "Pekerjaan : $pekerjaanPasien"
        goldar_pasien.text = "Gol.Darah : $goldarPasien"

        prefs["namaPasien"] = namaPasien
        prefs["tanggalLahirPasien"] = tanggalLahirPasien
        prefs["tempatLahirPasien"] = tempatLahirPasien
        prefs["jenisKelaminPasien"] = jenisKelaminPasien
        prefs["satusPerkawinan"] = satusPerkawinan
        prefs["alamatPasien"] = alamatPasien
        prefs["pekerjaanPasien"] = pekerjaanPasien
        prefs["goldarPasien"] = goldarPasien


    }

    private fun setDataPemesan() {
        nama_lengkap_pemesan.text = "Nama Lengkap : $namaPemesan"
        email_pemesan.text = "Alamat Email : $emailPemesan"
        no_telepon_pemesan.text = "Nomor Telepon : $noTelp"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isi_data)

        supportActionBar?.title = "Isi Data"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefs = PreferenceHelper.defaultPrefs(this)

        namaPemesan = prefs.getString("nama", "")
        emailPemesan = prefs.getString("email", "")
        noTelp = prefs.getString("noTelp", "")

        setDataPemesan()

        hospitalName = intent?.extras?.getString("hospitalName")
        jenisKamar = intent?.extras?.getString("jenisKamar")
        hargaKamar = intent?.extras?.getString("hargaKamar")

        nama_hospital.text = hospitalName
        jenis_kamar.text = jenisKamar

        val date = Calendar.getInstance().time
        val dateFormater = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        tanggalPesan = dateFormater.format(date)


        tanggal_pesan.text = tanggalPesan

        tanggal_pesan.setOnClickListener {
            showDateDialog()
        }

        tv_ubah_pemesan.setOnClickListener {
            showDataPemesanDialog()
        }

        tv_ubah_pasien.setOnClickListener {
            showDataPasienDialog()
        }

        button_next.setOnClickListener {
            if(namaPasien.isBlank()){
                Toast.makeText(this, "Tolong masukkan data pasien", Toast.LENGTH_SHORT).show()
            }else{
                navigateToReviewPemesanan()
            }
        }

    }


    private fun showDateDialog() {

        val newCalendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
            view, year, monthOfYear, dayOfMonth ->

            val newDate = Calendar.getInstance()
            newDate.set(year, monthOfYear, dayOfMonth)

            dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)

            tanggalPesan = dateFormatter!!.format(newDate.time)
            tanggal_pesan.text = dateFormatter!!.format(newDate.time)
        },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog!!.show()
    }


    private fun navigateToReviewPemesanan() {
        prefs["hospitalName"] = hospitalName
        prefs["jenisKamar"] = jenisKamar
        prefs["hargaKamar"] = hargaKamar
        prefs["tanggalPesan"] = tanggalPesan
        prefs["namaPemesan"] = namaPemesan
        prefs["emailPemesan"] = emailPemesan
        prefs["noTelpPemesan"] = noTelp
        val intent = Intent(this@IsiDataActivity, ReviewPemesananActivity::class.java)
        startActivity(intent)
    }


    private fun showDataPasienDialog() {
        val dataPemesanDialog = DataPasienDialog()
        dataPemesanDialog.show(supportFragmentManager,
                "edit_pasien_dialog_fragment")
    }

    private fun showDataPemesanDialog() {
        val dataPemesanDialog = DataPemesanDialog()
        dataPemesanDialog.show(supportFragmentManager,
                "edit_pemesan_dialog_fragment")
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
