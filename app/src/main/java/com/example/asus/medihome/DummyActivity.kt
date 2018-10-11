package com.example.asus.medihome


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.asus.medihome.model.Hospital
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation

import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*

class DummyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_location)

        val hospitalRef = FirebaseDatabase.getInstance().reference.child("hospital")

        val hospitalId = hospitalRef.push().key
        val hospital = Hospital(hospitalId!!, "Rumah Sakit Permata Bunda",
                "0341487487",
                "Jl. Soekarno Hatta No.75, Mojolangu, Kec. Lowokwaru, Kota Malang, Jawa Timur 65142", "",
                -7.938895999999999, 112.6248248)

        val hospitalId2 = hospitalRef.push().key
        val hospital2 = Hospital(hospitalId2!!, "Rumah Sakit Universitas Brawijaya",
                "0341403000",
                "Jl. Soekarno - Hatta, Lowokwaru, Kec. Lowokwaru, Kota Malang, Jawa Timur 65141", "",
                -7.941227499999999, 112.6215363)

        hospitalRef.child(hospitalId).setValue(hospital).addOnCompleteListener { task ->
            hospitalRef.child(hospitalId2).setValue(hospital2).addOnCompleteListener{task ->
                hospitalRef.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                         for(data in p0.children){
                             val hospitalId = data.child("hospitalId").value.toString()
                             val lat = data.child("lat").value.toString().toDouble()
                             val lng = data.child("lng").value.toString().toDouble()

                             val geoFire = GeoFire(FirebaseDatabase.getInstance()
                                     .reference.child("geofire"))

                             geoFire.setLocation(hospitalId, GeoLocation(lat, lng)
                             ) { key, error ->
                                 if(error != null){
                                     Toast.makeText(applicationContext,
                                             "Kesalahan "+error.message, Toast.LENGTH_SHORT).show()
                                 }
                             }

                         }
                    }

                })
            }
        }



    }
}
