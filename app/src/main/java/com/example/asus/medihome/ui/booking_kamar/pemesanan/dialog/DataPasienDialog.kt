package com.example.asus.medihome.ui.booking_kamar.pemesanan.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.example.asus.medihome.R
import kotlinx.android.synthetic.main.data_pasien_dialog.*
import java.text.SimpleDateFormat
import java.util.*


class DataPasienDialog : DialogFragment(){

    lateinit var mListener : OnDataPasienSaved

    private var datePickerDialog: DatePickerDialog? = null
    private var dateFormatter: SimpleDateFormat? = null

    var tanggalLahirPasien: String = ""

    interface OnDataPasienSaved{
        fun sendDataPasien(namaPasien : String, tanggalLahirPasien : String, tempatLahirPasien : String,
                           jenisKelaminPasien : String, satusPerkawinan : String, alamatPasien : String,
                           pekerjaanPasien : String, goldarPasien : String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.data_pasien_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)

        button_close.setOnClickListener {
            dialog.dismiss()
        }

        simpan_btn.setOnClickListener {
            getDataPasien(view)
        }

        tanggal_lahir_tv.setOnClickListener {
            showDateDialog()
        }

    }

    private fun getDataPasien(view: View) {
        val nama = nama_lengkap_et.text.toString().trim()
        val tempatLahir = tempat_lahir_et.text.toString().trim()
        val statusPerkawinan = status_et.text.toString().trim()
        val alamat = alamat_et.text.toString().trim()
        val pekerjaan = pekerjaan_et.text.toString().trim()

        val idxJenisKelamin = rg_jenis_kelamin.checkedRadioButtonId
        val rbJenisKelamin = view.findViewById<RadioButton>(idxJenisKelamin)

        val idxGoldar = rg_goldar.checkedRadioButtonId
        val rbGoldar = view.findViewById<RadioButton>(idxGoldar)

        if(nama.isBlank() || tempatLahir.isBlank() || statusPerkawinan.isBlank()
                || alamat.isBlank() || pekerjaan.isBlank()){
            Toast.makeText(context, "Masukkan semua data" , Toast.LENGTH_SHORT).show()
        }else{
            if(rbJenisKelamin == null){
                Toast.makeText(context, "Pilih jenis kelamin" , Toast.LENGTH_SHORT).show()
            }else if(rbGoldar == null){
                Toast.makeText(context, "Pilih golongan darah" , Toast.LENGTH_SHORT).show()
            }else{
                val jenisKelamin = rbJenisKelamin.text
                val goldar = rbGoldar.text

                mListener.sendDataPasien(nama, tanggalLahirPasien, tempatLahir, jenisKelamin.toString(),
                        statusPerkawinan, alamat, pekerjaan, goldar.toString())

                dialog.dismiss()
            }
        }

    }


    private fun showDateDialog() {

        val newCalendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener {
            view, year, monthOfYear, dayOfMonth ->

            val newDate = Calendar.getInstance()
            newDate.set(year, monthOfYear, dayOfMonth)

            dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)

            tanggalLahirPasien = dateFormatter!!.format(newDate.getTime())
            tanggal_lahir_tv.text = dateFormatter!!.format(newDate.getTime())
        },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog!!.show()
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
            this.mListener = activity as OnDataPasienSaved
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnCompleteListener")
        }

    }

}