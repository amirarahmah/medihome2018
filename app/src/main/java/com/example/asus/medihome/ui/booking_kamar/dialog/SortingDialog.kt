package com.example.asus.medihome.ui.booking_kamar.dialog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome.R
import kotlinx.android.synthetic.main.sorting_dialog.*

class SortingDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.sorting_dialog, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutup_btn.setOnClickListener {
            dialog.dismiss()
        }

        harga_tertinggi_tv.setOnClickListener {
            dialog.dismiss()
        }

        harga_terendah_tv.setOnClickListener {
            dialog.dismiss()
        }

        popularitas_tv.setOnClickListener {
            dialog.dismiss()
        }

    }

    companion object {

        fun newInstance(): SortingDialog {
            return SortingDialog()
        }
    }

}