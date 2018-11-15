package com.example.asus.medihome.ui.mediclinic.dialog

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome.R
import com.example.asus.medihome.model.LayananDetaill
import com.example.asus.medihome.ui.mediclinic.adapter.DetailLayananAdapter
import com.example.asus.medihome.ui.mediclinic.reservasi.ReservasiActivity
import kotlinx.android.synthetic.main.dialog_data_pasien.*
import kotlinx.android.synthetic.main.dialog_ketentuan.*
import kotlinx.android.synthetic.main.fragment_kamar.*

class LayananDetailDialog : DialogFragment() {

    lateinit var mAdapter: DetailLayananAdapter
    lateinit var layananList: ArrayList<LayananDetaill>

    var namaKlinik: String? = ""
    var alamatKlinik: String? = ""
    var idKlinik: String? = ""

    lateinit var alertDialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dialog_layanan_detail, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mArgs = arguments
        idKlinik = mArgs?.getString("idKlinik")
        namaKlinik = mArgs?.getString("namaKlinik")
        alamatKlinik = mArgs?.getString("alamatKlinik")

        button_close.setOnClickListener {
            dialog.dismiss()
        }

        layananList = arrayListOf(LayananDetaill("", "Tambal Gigi Depan Patah",
                "Rp300.000"),
                LayananDetaill("", "Tambal Sementara",
                        "Rp80.000"),
                LayananDetaill("", "Tambal Amalgam",
                        "Rp150.000"),
                LayananDetaill("", "Tambal Resain Komposit Laser",
                        "Rp400.000"),
                LayananDetaill("", "Glass Lonomer Cement (GIC)",
                        "Rp400.000"))


        setupRecyclerView()

    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = DetailLayananAdapter(layananList
        ) { layanan -> onReservasiClicked(layanan) }

        recyclerView.adapter = mAdapter
    }


    private fun onReservasiClicked(layanan: LayananDetaill) {
        showAlertDialog(layanan)
    }

    private fun showAlertDialog(layanan: LayananDetaill) {
        val dialogBuilder = AlertDialog.Builder(context!!)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_ketentuan, null)
        dialogBuilder.setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("Setuju") { dialogInterface, i ->
                    navigateToReservasiActivity(layanan)
                }.setNegativeButton("Batal") { dialogInterface, i ->
                    alertDialog.dismiss()
                }
        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun navigateToReservasiActivity(layanan: LayananDetaill) {
        val intent = Intent(context, ReservasiActivity::class.java)
        intent.putExtra("idKlinik", idKlinik)
        intent.putExtra("namaLayanan", layanan.namaLayanan)
        intent.putExtra("hargaLayanan", layanan.harga)
        intent.putExtra("namaKlinik", namaKlinik)
        intent.putExtra("alamatKlinik", alamatKlinik)
        startActivity(intent)
    }

    private fun showKetentuanDetailDialog() {

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

    companion object {

        fun newInstance(): LayananDetailDialog {
            return LayananDetailDialog()
        }
    }

}