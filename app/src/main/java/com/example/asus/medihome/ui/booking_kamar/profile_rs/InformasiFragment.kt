package com.example.asus.medihome.ui.booking_kamar.profile_rs


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.asus.medihome.R
import com.example.asus.medihome.model.Fasilitas
import com.example.asus.medihome.model.Hospital
import com.example.asus.medihome.ui.booking_kamar.adapter.HospitalAdapter
import com.example.asus.medihome.ui.booking_kamar.profile_rs.adapter.FasilitasAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_informasi.*

class InformasiFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap : GoogleMap
    private lateinit var mAdapter : FasilitasAdapter
    private lateinit var mFasilitasList : ArrayList<Fasilitas>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_informasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        val alamatHospital = activity?.intent?.extras?.getString("alamatFull")
        val phoneHospital = activity?.intent?.extras?.getString("nomorTelpon")

        alamat_hospital.text = alamatHospital
        phone_hospital.text = phoneHospital

        mFasilitasList = arrayListOf(Fasilitas("1", "AC", R.drawable.medihome_aircond_icon),
                Fasilitas("2", "Lift", R.drawable.medihome_elevator_icon),
                Fasilitas("3", "Parkir", R.drawable.medihome_parking_icon),
                Fasilitas("4", "P3K", R.drawable.medihome_firstaid_icon))

        setupRecyclerView()


    }

    private fun setupRecyclerView() {
        fasilitas_recyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        mAdapter = FasilitasAdapter(mFasilitasList)
        fasilitas_recyclerView.adapter = mAdapter
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
