package com.example.asus.medihome.ui.authentification

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.asus.medihome.MainActivity
import com.example.asus.medihome.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import com.lmntrx.android.library.livin.missme.ProgressDialog


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

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
                        if(task.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
        }else if (email.isEmpty() && password.isEmpty()){
            Toast.makeText(this,"Mohon masukan email dan password", Toast.LENGTH_SHORT).show()
        }else if (email.isEmpty()){
            Toast.makeText(this,"Mohon masukan email", Toast.LENGTH_SHORT).show()
        }else if (password.isEmpty()){
            Toast.makeText(this,"Mohon masukan password", Toast.LENGTH_SHORT).show()
        }

    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        currentUser?.let {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}