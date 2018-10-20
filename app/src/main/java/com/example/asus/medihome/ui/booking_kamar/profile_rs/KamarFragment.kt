package com.example.asus.medihome.ui.booking_kamar.profile_rs

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Kamar
import com.example.asus.medihome.ui.booking_kamar.adapter.KamarAdapter
import com.example.asus.medihome.ui.booking_kamar.pemesanan.IsiDataActivity
import kotlinx.android.synthetic.main.fragment_kamar.*

class KamarFragment : Fragment() {

    lateinit var mAdapter: KamarAdapter
    lateinit var kamarList: ArrayList<Kamar>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kamar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        kamarList = arrayListOf()

        kamarList.add(Kamar("1", "VIP", "1 bed pasien",
                "500.000", 3, "http://hospital.umm.ac.id/files/image/Kelas%20Kemuning.JPG"))

        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = KamarAdapter(kamarList,
                { kamar -> onPesanKamarClicked(kamar) },
                { kamar -> onKamarClicked(kamar) })

        recyclerView.adapter = mAdapter
    }

    private fun onKamarClicked(kamar: Kamar) {
        val hospitalName = activity?.intent?.extras?.getString("nama")
        val intent = Intent(context, DetailKamarActivity::class.java)
        intent.putExtra("hospitalName", hospitalName)
        intent.putExtra("jenisKamar", kamar.jenisKamar)
        intent.putExtra("hargaKamar", kamar.harga)
        startActivity(intent)
    }

    private fun onPesanKamarClicked(kamar: Kamar) {
        val hospitalId = activity?.intent?.extras?.getString("hospitalId")
        val hospitalName = activity?.intent?.extras?.getString("nama")
        val intent = Intent(context, IsiDataActivity::class.java)
        intent.putExtra("hospitalId", hospitalId)
        intent.putExtra("hospitalName", hospitalName)
        intent.putExtra("kamarId", kamar.idKamar)
        intent.putExtra("jenisKamar", kamar.jenisKamar)
        intent.putExtra("hargaKamar", kamar.harga)
        startActivity(intent)
    }

}
