package com.example.asus.medihome.ui.mediclinic.reservasi

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.asus.medihome.R
import com.example.asus.medihome.util.setupView
import kotlinx.android.synthetic.main.dialog_pilihan_jadwal.*
import java.text.SimpleDateFormat
import java.util.*
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager



class PilihJadwalDialog : DialogFragment(){

    lateinit var mListener : OnJadwalSaved

    lateinit var mAdapter : JadwalAdapter
    var listJadwal = arrayListOf<String>()

    private var datePickerDialog: DatePickerDialog? = null
    private var dateFormatter: SimpleDateFormat? = null

    var dokter = ""
    var tanggal = ""
    var selectedJadwal = ""


    interface OnJadwalSaved{
        fun sendJadwal(dokter : String, tanggal : String, jadwal : String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_pilihan_jadwal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)

        setupSpinnerDokter()

        listJadwal = arrayListOf("10:00 - 12:00", "13:00 - 16-00", "16:00-19:00", "19:00-21:00")

        setupRecyclerView()

        button_date.setOnClickListener {
            showDateDialog()
        }

        button_close.setOnClickListener {
            dialog.dismiss()
        }

        simpan_btn.setOnClickListener {
            if(tanggal.isBlank() || selectedJadwal.isBlank()){
                Toast.makeText(context, "Pilih jadwal yang tersedia $selectedJadwal", Toast.LENGTH_SHORT).show()
            }else{
                mListener.sendJadwal(dokter, tanggal, selectedJadwal)
                dialog.dismiss()
            }
        }


    }

    private fun setupSpinnerDokter() {
        val listDokter = arrayListOf("drg. Iqbal Putra, Sp.BM", "drg. Amira Fauzia, Sp.KG",
                "drg. Martin Siahaan, Sp.Ort")
        spinner_dokter.setupView(listDokter)

        spinner_dokter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                dokter = parent?.getItemAtPosition(position).toString()
            }

        }
    }


    private fun setupRecyclerView() {
        val flowLayoutManager = FlowLayoutManager()
        flowLayoutManager.isAutoMeasureEnabled = true
        recyclerView.layoutManager = flowLayoutManager
        recyclerView.itemAnimator = null
        mAdapter = JadwalAdapter(context!!, listJadwal) { onJadwalClicked(it) }
        recyclerView.adapter = mAdapter
    }


    private fun onJadwalClicked(it: String) {
        selectedJadwal = it
    }


    private fun showDateDialog() {

        val newCalendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener {
            view, year, monthOfYear, dayOfMonth ->

            val newDate = Calendar.getInstance()
            newDate.set(year, monthOfYear, dayOfMonth)

            dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)

            tanggal = dateFormatter!!.format(newDate.getTime())
            button_date.text = dateFormatter!!.format(newDate.getTime())
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
            this.mListener = activity as OnJadwalSaved
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnCompleteListener")
        }

    }

}