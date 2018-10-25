package com.example.asus.medihome.ui.authentification

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.asus.medihome.MainActivity
import com.example.asus.medihome.R
import com.example.asus.medihome.model.User
import com.example.asus.medihome.model.UserProfile
import com.example.asus.medihome.util.PreferenceHelper
import com.example.asus.medihome.util.PreferenceHelper.set
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lmntrx.android.library.livin.missme.ProgressDialog
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Silahkan Menunggu..")
        progressDialog.setCancelable(false)

        login_btn.setOnClickListener {
            loginUser()
        }

        daftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }


    private fun loginUser() {

        val email = email_et.text.toString().trim()
        val password = password_et.text.toString().trim()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            progressDialog.show()
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        progressDialog.dismiss()
                        if (task.isSuccessful) {

                            getDataUser()
                        } else {
                            val errorCode = (task.exception as FirebaseAuthException).errorCode

                            when (errorCode) {

                                "ERROR_WRONG_PASSWORD" ->
                                    Toast.makeText(this, "Password yang anda masukan salah",
                                            Toast.LENGTH_LONG).show()

                                "ERROR_USER_NOT_FOUND" ->
                                    Toast.makeText(this, "Akun belum terdaftar",
                                            Toast.LENGTH_LONG).show()
                                else ->
                                    Toast.makeText(this, ""+
                                            (task.exception as FirebaseAuthException).message,
                                            Toast.LENGTH_LONG).show()
                            }
                        }
                    }
        } else if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Mohon masukan email dan password", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Mohon masukan email", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Mohon masukan password", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getDataUser() {
        val userRef = FirebaseDatabase.getInstance().reference.child("users")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        userRef.child(userId!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)

                val nama = user?.nama
                val email = user?.email
                val nomorTelpon = user?.nomorTelpon

                val prefs = PreferenceHelper.defaultPrefs(this@LoginActivity)

                prefs["nama"] = nama
                prefs["email"] = email
                prefs["noTelp"] = nomorTelpon

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

            }
        })
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        currentUser?.let {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}
