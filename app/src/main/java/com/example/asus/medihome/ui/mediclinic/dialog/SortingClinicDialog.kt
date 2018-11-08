package com.example.asus.medihome.ui.mediclinic.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome.R
import kotlinx.android.synthetic.main.sorting_clinic_dialog.*

class SortingClinicDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.sorting_clinic_dialog, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutup_btn.setOnClickListener {
            dialog.dismiss()
        }

        popularitas_tv.setOnClickListener {
            dialog.dismiss()
        }

        jarak_terdekat_tv.setOnClickListener {
            dialog.dismiss()
        }

        jam_buka_awal_tv.setOnClickListener {
            dialog.dismiss()
        }

        jam_buka_akhir_tv.setOnClickListener {
            dialog.dismiss()
        }


    }

    companion object {

        fun newInstance(): SortingClinicDialog {
            return SortingClinicDialog()
        }
    }

}