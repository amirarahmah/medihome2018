package com.example.asus.medihome.ui.pesanan_detail

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.asus.medihome.MainActivity
import com.example.asus.medihome.R
import com.example.asus.medihome.extension.toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.enums.EPickType
import kotlinx.android.synthetic.main.activity_transfer_clinic.*

class TransferClinicActivity : AppCompatActivity() {

    var imageUri: Uri? = null
    private var cTimer : CountDownTimer? = null
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_clinic)

        supportActionBar?.title = "Transfer"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val totalPembayaran = intent?.extras?.getString("hargaLayanan")
        val idReservation = intent?.extras?.getString("idReservation")

        total_pembayaran.text = "$totalPembayaran"

        startTimer()

        val setup = PickSetup()
                .setTitle("Pilih Gambar")
                .setMaxSize(500)
                .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)

        bukti_pembayaran.setOnClickListener {
            PickImageDialog.build(setup)
                    .setOnPickResult { pickResult ->
                        val imageBitmap = pickResult.bitmap
                        imageUri = pickResult.uri
                        bukti_pembayaran.setImageBitmap(imageBitmap)
                    }
                    .setOnPickCancel { }
                    .show(this)
        }

        button_done.setOnClickListener {
            if(imageUri != null){
                uploadBuktiPembayaran(idReservation)
            }else{
                toast("Mohon upload bukti pembayaran")
            }
        }

    }

    private fun uploadBuktiPembayaran(idReservation: String?) {
        showProgressDialog()
        val storageReference = FirebaseStorage.getInstance().reference
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val filepath = storageReference.child("buktiPembayaran").child("$idReservation.jpg")

        val uploadTask = filepath.putFile(imageUri!!)
        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation filepath.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                alertDialog.dismiss()
                navigateToMainActivity()
            } else {
                alertDialog.dismiss()
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showProgressDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.progress_dialog, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        alertDialog = dialogBuilder.create()
        alertDialog.show()
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

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}
