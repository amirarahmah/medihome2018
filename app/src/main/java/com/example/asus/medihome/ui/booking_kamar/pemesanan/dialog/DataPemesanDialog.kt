package com.example.asus.medihome.ui.booking_kamar.pemesanan.dialog

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.asus.medihome.R
import kotlinx.android.synthetic.main.dialog_data_pemesan.*

class DataPemesanDialog : DialogFragment(){

    interface OnDataSaved{
        fun sendDataPemesan(namaLengkap : String, email : String, phone : String)
    }

    lateinit var mListener : OnDataSaved


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_data_pemesan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_close.setOnClickListener {
            dialog.dismiss()
        }

        simpan_btn.setOnClickListener {
            val nama = nama_lengkap_et.text.toString().trim()
            val email = email_et.text.toString().trim()
            val phone = nomor_telepon_et.text.toString().trim()

            if(nama.isBlank() || email.isBlank() || phone.isBlank()){
                Toast.makeText(context, "Tolong masukkan semua data", Toast.LENGTH_SHORT).show()
            }else{
                mListener.sendDataPemesan(nama, email, phone)
                dialog.dismiss()
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setWindowAnimations(R.style.DialogAnimation)
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            this.mListener = activity as OnDataSaved
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnCompleteListener")
        }

    }

}