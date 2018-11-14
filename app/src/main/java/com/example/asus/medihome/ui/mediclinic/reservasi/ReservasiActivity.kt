package com.example.asus.medihome.ui.mediclinic.reservasi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.asus.medihome.MainActivity
import com.example.asus.medihome.R
import com.example.asus.medihome.api.Data
import com.example.asus.medihome.api.NotifReservasi
import com.example.asus.medihome.api.NotifResponse
import com.example.asus.medihome.api.NotifService
import com.example.asus.medihome.model.Reservation
import com.example.asus.medihome.model.Userclinic
import com.example.asus.medihome.ui.booking_kamar.pemesanan.dialog.DataPasien2Dialog
import com.example.asus.medihome.ui.booking_kamar.pemesanan.dialog.DataPemesanDialog
import com.example.asus.medihome.util.Constant
import com.example.asus.medihome.util.PreferenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lmntrx.android.library.livin.missme.ProgressDialog
import kotlinx.android.synthetic.main.activity_reservasi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.SecureRandom

class ReservasiActivity : AppCompatActivity(), DataPemesanDialog.OnDataSaved,
        DataPasien2Dialog.OnDataPasienSaved, PilihJadwalDialog.OnJadwalSaved {


    private lateinit var prefs: SharedPreferences
    private lateinit var progressDialog: ProgressDialog

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
    var emailPasien : String = ""
    var noTelpPasien : String = ""
    var jenisKelaminPasien : String = ""
    var tglLahirPasien : String = ""
    var noRekam : String = ""

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
        this.emailPasien = email
        this.noTelpPasien = phone
        this.noRekam = noRekam

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

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Silahkan Menunggu..")
        progressDialog.setCancelable(false)

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
        progressDialog.show()
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
        val pesan = pesan_et.text.toString()

        val reservasi = Reservation(idReservasi, idKlinik!!, userId!!, namaKlinik!!, alamatKlinik!!,
                namaLayanan!!, namaDokter, hargaLayanan!!, tanggalReservasi, pukul, namaPasien,
                emailPasien, noTelpPasien, jenisKelaminPasien, tglLahirPasien,
                noRekam, pesan)


        reservasiRef.child(idReservasi).setValue(reservasi).addOnCompleteListener {task ->
            if(task.isSuccessful){
                sendNotificationToClinic(idKlinik, idReservasi)
            }else{
                progressDialog.dismiss()
                Toast.makeText(this, ""+task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun sendNotificationToClinic(idKlinik: String, idReservasi: String) {
        val clinicRef = FirebaseDatabase.getInstance().reference.child("userclinic")
        clinicRef.orderByChild("clinicId").equalTo(idKlinik)
                .addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        progressDialog.dismiss()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for (data in p0.children) {
                            val userClinic = data.getValue(Userclinic::class.java)
                            val token = userClinic?.token

                            val notifData = Data(idReservasi, "Lihat untuk konfirmasi",
                                    "Anda memiliki reservasi baru")
                            val notif = NotifReservasi(token!!, notifData)

                            NotifService.create().sendNotifToClinic(notif, "key=${Constant.CLOUD_MESSAGE_KEY}")
                                    .enqueue(object : Callback<NotifResponse> {
                                        override fun onFailure(call: Call<NotifResponse>, t: Throwable) {
                                            progressDialog.dismiss()
                                        }

                                        override fun onResponse(call: Call<NotifResponse>, response: Response<NotifResponse>) {
                                            progressDialog.dismiss()
                                            Toast.makeText(this@ReservasiActivity,
                                                    "Reservasi berhasil dilakukan", Toast.LENGTH_SHORT).show()
                                            navigateToMainActivity()
                                        }

                                    })

                        }
                    }
                })
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
