package com.example.asus.medihome.ui.mediclinic.profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Layanan
import com.example.asus.medihome.ui.mediclinic.adapter.LayananAdapter
import com.example.asus.medihome.ui.mediclinic.dialog.LayananDetailDialog
import kotlinx.android.synthetic.main.fragment_layanan_klinik.*


class LayananKlinikFragment : Fragment() {

    lateinit var mAdapter: LayananAdapter
    lateinit var layananList: ArrayList<Layanan>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_layanan_klinik, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val kategoriKlinik = activity?.intent?.extras?.getString("kategoriKlinik")

        layananList = arrayListOf()

        if(kategoriKlinik.equals("Gigi", true)){
            layananList.add(Layanan("1", "Tambal Gigi", "Rp 50.000 - Rp 400.000",
                    "http://www.odhaberhaksehat.org/wp-content/uploads/2015/09/8402Gentle-Dental.jpg"))
        }

        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = LayananAdapter(layananList
        ) { layanan -> onReservasiClicked(layanan) }

        recyclerView.adapter = mAdapter
    }


    private fun onReservasiClicked(layanan: Layanan) {
        val namaKlinik = activity?.intent?.extras?.getString("nama")
        val args = Bundle()
        args.putString("namaKlinik", namaKlinik)
        val dialog = LayananDetailDialog.newInstance()
        dialog.arguments = args
        val fragmentManager = childFragmentManager
        dialog.show(fragmentManager, "sorting_fragment")
    }


}
