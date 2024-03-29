package com.example.asus.medihome.ui.authentification

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.asus.medihome.MainActivity
import com.example.asus.medihome.R
import com.example.asus.medihome.model.User
import com.example.asus.medihome.util.PreferenceHelper
import com.example.asus.medihome.util.PreferenceHelper.set
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lmntrx.android.library.livin.missme.ProgressDialog
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var userRef : DatabaseReference

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userRef = FirebaseDatabase.getInstance().reference.child("users")
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Silahkan Menunggu..")
        progressDialog.setCancelable(false)

        register_btn.setOnClickListener {
            validateForm()
        }

        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

    }

    //validate user input
    private fun validateForm() {
        val email = email_et.text.toString().trim()
        val nama = namalengkap_et.text.toString().trim()
        val nomorTelpon = notelp_et.text.toString().trim()
        val password = password_et.text.toString().trim()
        val confirmPassword = confirm_password_et.text.toString().trim()

        if(inputNotEmpty(email, nama, nomorTelpon, password, confirmPassword)){
            if(password.equals(confirmPassword)){
                registerUser(email, nama, nomorTelpon, password)
            }else{
                Toast.makeText(applicationContext, "Kata Sandi tidak cocok", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Mohon masukan semua data", Toast.LENGTH_SHORT).show()
        }

    }



    private fun registerUser(email: String, nama: String, nomorTelpon: String, password: String) {
        progressDialog.show()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val userId = auth.currentUser!!.uid
                val newUser = User(userId, email,  nama, nomorTelpon)

                //send user data to firebase database
                userRef.child(userId).setValue(newUser).addOnCompleteListener{task ->
                    progressDialog.dismiss()

                    val prefs = PreferenceHelper.defaultPrefs(this)

                    prefs["nama"] = nama
                    prefs["email"] = email
                    prefs["noTelp"] = nomorTelpon

                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }

            }else{
                progressDialog.dismiss()
                val errorCode = (task.exception as FirebaseAuthException).errorCode

                when (errorCode) {

                    "ERROR_WEAK_PASSWORD" ->
                        Toast.makeText(this, "Password harus memiliki minimal 6 karakter",
                                Toast.LENGTH_LONG).show()

                    "ERROR_EMAIL_ALREADY_IN_USE" ->
                        Toast.makeText(this, "Email sudah digunakan oleh akun lain",
                                Toast.LENGTH_LONG).show()
                    else ->
                        Toast.makeText(this, ""+
                                (task.exception as FirebaseAuthException).message,
                                Toast.LENGTH_LONG).show()
                }
            }
        }

    }


    private fun inputNotEmpty(email: String, nama: String, nomorTelpon: String,
                              password: String, confirmPassword: String): Boolean {

        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(nama)
                || TextUtils.isEmpty(nomorTelpon) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPassword))
    }

}
