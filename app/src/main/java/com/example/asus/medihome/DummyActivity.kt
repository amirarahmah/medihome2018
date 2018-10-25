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
        val hospital = Hospital(hospitalId!!, "RS Islam Siti Hajar",
                "0318921233", "",
                "Jasem, Bulusidokare, Sidoarjo", "", "",
                -7.457630, 112.722055)

        val hospitalId2 = hospitalRef.push().key
        val hospital2 = Hospital(hospitalId2!!, "RS Delta Surya",
                "0318962531", "",
                "Jati, Sidoarjo", "", "",
                -7.447080, 112.701572)

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
