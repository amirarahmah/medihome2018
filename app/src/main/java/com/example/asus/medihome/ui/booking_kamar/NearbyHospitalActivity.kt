package com.example.asus.medihome.ui.booking_kamar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asus.medihome.R
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.asus.medihome.model.Hospital
import com.example.asus.medihome.ui.booking_kamar.adapter.HospitalAdapter
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_nearby_hospital.*

class NearbyHospitalActivity : AppCompatActivity() {

    val REQUEST_LOCATION = 900
    private val REQUEST_CHECK_SETTINGS = 100

    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mLocationRequest : LocationRequest
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    lateinit var geoFire: GeoFire
    lateinit var hospitalRef : DatabaseReference

    var listHospitals: ArrayList<Hospital> = arrayListOf()
    lateinit var mAdapter : HospitalAdapter
    lateinit var nama : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_hospital)

        supportActionBar?.title = "Rumah Sakit di Sekitar Anda"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                val location = locationResult.lastLocation
                getNearbyHospital(location)
            }
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setupFirebase()
        setupRecyclerView()
        checkLocationSetting()

    }


    private fun setupFirebase() {
        geoFire = GeoFire(FirebaseDatabase.getInstance()
                .reference.child("geofire"))
        hospitalRef = FirebaseDatabase.getInstance().reference.child("hospital")
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = HospitalAdapter(listHospitals)
        recyclerView.adapter = mAdapter
    }


    private fun getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION)
        } else {
            //permission granted
            mFusedLocationProviderClient.lastLocation
                    .addOnSuccessListener(this) { location ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            getNearbyHospital(location)
                        } else {
                            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, null)
                        }
                    }
        }
    }



    private fun getNearbyHospital(location : Location){

        val geoQuery = geoFire.queryAtLocation(GeoLocation(location.latitude, location.longitude),50.0)
        geoQuery.addGeoQueryEventListener(object : GeoQueryEventListener{
            override fun onGeoQueryReady() {

            }

            override fun onKeyMoved(key: String?, location: GeoLocation?) {

            }

            override fun onKeyExited(key: String?) {

            }

            override fun onGeoQueryError(error: DatabaseError) {
                Toast.makeText(this@NearbyHospitalActivity, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onKeyEntered(key: String, location: GeoLocation?) {
                hospitalRef.child(key).addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val hospital = p0.getValue(Hospital::class.java)
                        listHospitals.add(hospital!!)

                        mAdapter.notifyDataSetChanged()
                    }

                })
            }
        })


    }



    private fun checkLocationSetting() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
        builder.setAlwaysShow(true)


        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task.addOnCompleteListener { task ->
            try {
                // All location settings are satisfied. The client can initialize location
                val response = task.getResult(ApiException::class.java)
                getDeviceLocation()

            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                    this,
                                    REQUEST_CHECK_SETTINGS)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }// Location settings are not satisfied. However, we have no way to fix the
                // settings so we won't show the dialog.
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val states = LocationSettingsStates.fromIntent(data!!)

        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    getDeviceLocation()
                }
                Activity.RESULT_CANCELED -> {
                    // The user was asked to change settings, but chose not to

                }
                else -> {

                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_LOCATION){
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                getDeviceLocation()
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }


    override fun onPause() {
        super.onPause()
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

}
