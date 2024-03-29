package com.example.asus.medihome


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.asus.medihome.model.Hospital
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DummyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_location)

        val hospitalRef = FirebaseDatabase.getInstance().reference.child("hospital")

        val hospitalId = hospitalRef.push().key
        val hospital = Hospital(hospitalId!!, "Tung Shin Hospital",
                "0320372288", "Kuala Lumpur",
                "Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur",
                "102, Jalan Pudu, Bukit Bintang, Kuala Lumpur",
                "", 3.146454, 101.704076)

        val hospitalId2 = hospitalRef.push().key
        val hospital2 = Hospital(hospitalId2!!, "Pantai Hospital Ampang",
                "0342892828", "Kuala Lumpur",
                "Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur",
                "Jalan Perubatan 1, Pandan Indah, Kuala Lumpur",
                "", 3.127759, 101.752081)

        val hospitalId3 = hospitalRef.push().key
        val hospital3 = Hospital(hospitalId3!!, "Kuala Lumpur Hospital",
                "0326155555", "Kuala Lumpur",
                "Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur",
                "23, Jalan Pahang, Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur",
                "", 3.172666, 101.701487)

        hospitalRef.child(hospitalId).setValue(hospital).addOnCompleteListener { task ->
            hospitalRef.child(hospitalId2).setValue(hospital2).addOnCompleteListener { task ->
                hospitalRef.child(hospitalId3).setValue(hospital3).addOnCompleteListener { task ->

                    hospitalRef.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            for (data in p0.children) {
                                val hospitalId = data.child("hospitalId").value.toString()
                                val lat = data.child("lat").value.toString().toDouble()
                                val lng = data.child("lng").value.toString().toDouble()

                                val geoFire = GeoFire(FirebaseDatabase.getInstance()
                                        .reference.child("geofire"))

                                geoFire.setLocation(hospitalId, GeoLocation(lat, lng)
                                ) { key, error ->
                                    if (error != null) {
                                        Toast.makeText(applicationContext,
                                                "Kesalahan " + error.message, Toast.LENGTH_SHORT).show()
                                    }
                                }

                            }
                        }

                    })

                }

            }
        }


    }
}
