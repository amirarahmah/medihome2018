package com.example.asus.medihome.ui.mediclinic.profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.asus.medihome.R
import com.example.asus.medihome.model.Fasilitas
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_informasi_klinik.*


class InformasiKlinikFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap : GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_informasi_klinik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        val alamatKlinik = activity?.intent?.extras?.getString("alamat")
        val phoneKlinik= activity?.intent?.extras?.getString("nomorTelpon")

        alamat_klinik.text = alamatKlinik
        phone_klinik.text = phoneKlinik


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val lat = activity?.intent?.extras?.getDouble("lat")
        val lng = activity?.intent?.extras?.getDouble("lng")
        val sydney = LatLng(lat!!, lng!!)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
    }

}
