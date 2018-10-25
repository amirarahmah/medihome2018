package com.example.asus.medihome.ui.booking_kamar

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Hospital
import com.example.asus.medihome.ui.booking_kamar.adapter.HospitalAdapter
import com.example.asus.medihome.ui.booking_kamar.profile_rs.ProfilRumahSakitActivity
import com.firebase.geofire.GeoFire
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_nearby_hospital.*

class HospitalListActivity : AppCompatActivity() {

    lateinit var hospitalRef: DatabaseReference

    var listHospitals: ArrayList<Hospital> = arrayListOf()
    lateinit var mAdapter: HospitalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_by_name)

        supportActionBar?.title = "Rumah Sakit"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val modeCari = intent?.extras?.getString("modeCari")

        setupFirebase()
        setupRecyclerView()

        hospitalRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                progressBar.visibility = View.GONE
            }
            override fun onDataChange(p0: DataSnapshot) {
                progressBar.visibility = View.GONE
                listHospitals.clear()
                for (data in p0.children) {
                    val hospitalId = data.child("hospitalId").value.toString()
                    val nama = data.child("nama").value.toString()
                    val nomorTelpon = data.child("nomorTelpon").value.toString()
                    val kota = data.child("kota").value.toString()
                    val alamat = data.child("alamat").value.toString()
                    val alamatFull = data.child("alamatFull").value.toString()
                    val photo = data.child("photo").value.toString()
                    val lat = data.child("lat").value.toString().toDouble()
                    val lng= data.child("lng").value.toString().toDouble()

                    if(modeCari.equals("nama")){
                        val namaRs = intent?.extras?.getString("namaRs")
                        if(nama.contains(namaRs!!, true)){
                            val hospital = Hospital(hospitalId, nama, nomorTelpon, kota,
                                    alamat, alamatFull, photo, lat, lng)
                            listHospitals.add(hospital)
                        }
                    }else{
                        val kotaExtras = intent?.extras?.getString("kota")
                        if(kotaExtras.equals(kota, true)){
                            val hospital = Hospital(hospitalId, nama, nomorTelpon, kota,
                                    alamat, alamatFull, photo, lat, lng)
                            listHospitals.add(hospital)
                        }
                    }

                }

                mAdapter.notifyDataSetChanged()

            }
        })

    }


    private fun setupFirebase() {
        hospitalRef = FirebaseDatabase.getInstance().reference.child("hospital")
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = HospitalAdapter(listHospitals) { hospital: Hospital -> hospitalClicked(hospital) }
        recyclerView.adapter = mAdapter
    }

    private fun hospitalClicked(hospital: Hospital) {
        val intent = Intent(this, ProfilRumahSakitActivity::class.java)
        intent.putExtra("hospitalId", hospital.hospitalId)
        intent.putExtra("nama", hospital.nama)
        intent.putExtra("nomorTelpon", hospital.nomorTelpon)
        intent.putExtra("alamat", hospital.alamat)
        intent.putExtra("alamatFull", hospital.alamatFull)
        intent.putExtra("lat", hospital.lat)
        intent.putExtra("lng", hospital.lng)
        intent.putExtra("photo", hospital.photo)
        startActivity(intent)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }

}
