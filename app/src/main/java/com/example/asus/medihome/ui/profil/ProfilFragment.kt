package com.example.asus.medihome.ui.profil


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.asus.medihome.R
import com.example.asus.medihome.model.User
import com.example.asus.medihome.model.UserProfile
import com.example.asus.medihome.ui.authentification.LoginActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.lmntrx.android.library.livin.missme.ProgressDialog
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.enums.EPickType
import kotlinx.android.synthetic.main.fragment_profil.*


class ProfilFragment : Fragment() {

    var imageUri: Uri? = null
    var photoUrl : String = ""
    private var profilChanged = false

    lateinit var alertDialog: AlertDialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_container.visibility = View.GONE

        keluarBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            navigateToLoginActivity()
        }

        val setup = PickSetup()
                .setTitle("Pilih Gambar")
                .setMaxSize(500)
                .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)

        image_profile.setOnClickListener {
            PickImageDialog.build(setup)
                    .setOnPickResult { pickResult ->
                        val imageBitmap = pickResult.bitmap
                        imageUri = pickResult.uri
                        image_profile.setImageBitmap(imageBitmap)
                    }
                    .setOnPickCancel { }
                    .show(fragmentManager)
        }

        val userRef = FirebaseDatabase.getInstance().reference.child("users")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        userRef.child(userId!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                progressBar.visibility = View.GONE
                profile_container.visibility = View.VISIBLE
                val user = p0.getValue(UserProfile::class.java)
                val nama = user?.nama
                val email = user?.email
                val tanggalLahir = user?.tanggalLahir
                val alamat = user?.alamat
                val jenisKelamin = user?.jenisKelamin
                val nomorTelpon = user?.nomorTelpon
                val goldar = user?.goldar
                photoUrl = user?.photoUrl!!

                nama_lengkap_et.setText(nama)
                email_et.setText(email)
                tanggal_lahir_et.setText(tanggalLahir)
                alamat_et.setText(alamat)
                jenis_kelamin_et.setText(jenisKelamin)
                nomor_telepon_et.setText(nomorTelpon)
                goldar_et.setText(goldar)

                if(!photoUrl.isBlank()){
                    Glide.with(context!!)
                            .load(photoUrl)
                            .into(image_profile)
                }

            }
        })

        ubahBtn.setOnClickListener {
            if (!profilChanged) {
                profilChanged = true
                ubahBtn.text = "Simpan"
                nama_lengkap_et.isEnabled = true
                email_et.isEnabled = true
                tanggal_lahir_et.isEnabled = true
                alamat_et.isEnabled = true
                jenis_kelamin_et.isEnabled = true
                nomor_telepon_et.isEnabled = true
                goldar_et.isEnabled = true
                nama_lengkap_et.requestFocus()
            } else {
                profilChanged = false
                ubahBtn.text = "Ubah"
                saveProfile()
            }

        }

    }

    private fun saveProfile() {
        showProgressDialog()
        if (imageUri != null) {
            uploadProfileImageToFirebase()
        } else {
            sendUserProfileToFirebase()
        }
    }

    private fun sendUserProfileToFirebase() {
        val nama = nama_lengkap_et.text.toString().trim()
        val email = email_et.text.toString().trim()
        val tanggalLahir = tanggal_lahir_et.text.toString().trim()
        val alamat = alamat_et.text.toString().trim()
        val jenisKelamin = jenis_kelamin_et.text.toString().trim()
        val nomorTelepon = nomor_telepon_et.text.toString().trim()
        val goldar = goldar_et.text.toString().trim()

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val mRef = FirebaseDatabase.getInstance().reference.child("users").child(userId!!)
        val newUserProfile = UserProfile(userId, email, nama, tanggalLahir, alamat, jenisKelamin,
                nomorTelepon, goldar, photoUrl)

        mRef.setValue(newUserProfile).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                alertDialog.dismiss()
                Toast.makeText(context, "Profile berhasil diubah", Toast.LENGTH_SHORT).show()
            }else {
                alertDialog.dismiss()
                Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }


        nama_lengkap_et.isEnabled = false
        email_et.isEnabled = false
        tanggal_lahir_et.isEnabled = false
        alamat_et.isEnabled = false
        jenis_kelamin_et.isEnabled = false
        nomor_telepon_et.isEnabled = false
        goldar_et.isEnabled = false


    }

    private fun uploadProfileImageToFirebase() {
        val storageReference = FirebaseStorage.getInstance().reference
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val filepath = storageReference.child("profile").child("$userId.jpg")

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
                photoUrl = task.result.toString()
                sendUserProfileToFirebase()
            } else {
                alertDialog.dismiss()
                Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun navigateToLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun showProgressDialog() {
        val dialogBuilder = AlertDialog.Builder(context!!)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.progress_dialog, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

}
