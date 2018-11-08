package com.example.asus.medihome.ui.booking_kamar.dialog

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome.R
import com.example.asus.medihome.ui.booking_kamar.adapter.LokasiAdapter
import kotlinx.android.synthetic.main.cari_lokasi_dialog.*
import kotlin.collections.ArrayList

class CariLokasiDialog : DialogFragment(){

    lateinit var mAdapter : LokasiAdapter
    lateinit var mListLokasi : ArrayList<String>
    lateinit var mListener : OnLokasiClicked


    interface OnLokasiClicked{
        fun sendLokasi(lokasi : String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cari_lokasi_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mListLokasi = arrayListOf("Malang", "Sidoarjo", "Tegal", "Bali")
        mAdapter = LokasiAdapter(mListLokasi) { lokasi -> onLokasiClicked(lokasi)}
        lokasi_recyclerView.layoutManager = LinearLayoutManager(context)
        lokasi_recyclerView.adapter = mAdapter

        search_lokasi.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                filterLokasi(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                
            }
        })

        button_close.setOnClickListener {
            dialog.dismiss()
        }

        rs_disekitar_anda.setOnClickListener {
            mListener.sendLokasi("nearby")
            dialog.dismiss()
        }

    }

    private fun filterLokasi(text: String) {
        val filteredList = arrayListOf<String>()
        for(s in mListLokasi){
            if (s.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(s)
            }
        }

        mAdapter.filterList(filteredList)
    }

    private fun onLokasiClicked(lokasi: String) {
        mListener.sendLokasi(lokasi)
        dialog.dismiss()
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
            this.mListener = activity as OnLokasiClicked
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnCompleteListener")
        }

    }

}