package com.example.asus.medihome.ui.emergency


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.asus.medihome.MainActivity
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Ambulans
import com.example.asus.medihome.model.Hospital
import com.example.asus.medihome.ui.emergency.adapter.EmergencyAdapter
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_ambulans.*
import java.lang.Exception


class EmergencyFragment : Fragment() {

    val REQUEST_LOCATION = 900
    val CALL_REQUEST = 800
    val REQUEST_CHECK_SETTINGS = 100

    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    lateinit var geoFire: GeoFire
    lateinit var hospitalRef: DatabaseReference

    var listHospitals: ArrayList<Ambulans> = arrayListOf()
    lateinit var mAdapter: EmergencyAdapter
    lateinit var nama: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ambulans, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                val location = locationResult.lastLocation
                getNearbyHospital(location)
                mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
            }
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)


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
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = EmergencyAdapter(listHospitals, context!!){showAlertDialog()}
        recyclerView.adapter = mAdapter
    }


    private fun showAlertDialog() {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setTitle("")
                .setMessage("Apakah Anda yakin ingin memanggil Ambulans?")
                .setPositiveButton("Ya") { dialog, which ->
                    makePhoneCall()
                }
                .setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }

        val alertDialog = alertBuilder.create()
        alertDialog.show()
    }

    private fun makePhoneCall() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), CALL_REQUEST)
                    return

                }
            }

            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + 811111111)
            startActivity(callIntent)
        } catch (ex : Exception) {
            ex.printStackTrace()
        }
    }


    private fun getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(context!!,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(context as AppCompatActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION)
        } else {
            progressBar.visibility = View.VISIBLE
            //permission granted
            mFusedLocationProviderClient.lastLocation
                    .addOnSuccessListener(context as AppCompatActivity) { location ->
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


    private fun getNearbyHospital(myLocation: Location) {
        val geoQuery = geoFire.queryAtLocation(GeoLocation(myLocation.latitude, myLocation.longitude), 50.0)
        geoQuery.addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onGeoQueryReady() {
                progressBar.visibility = View.GONE
            }

            override fun onKeyMoved(key: String?, location: GeoLocation?) {
                progressBar.visibility = View.GONE
            }

            override fun onKeyExited(key: String?) {
                progressBar.visibility = View.GONE
            }

            override fun onGeoQueryError(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onKeyEntered(key: String, location: GeoLocation?) {
                progressBar.visibility = View.VISIBLE
                hospitalRef.child(key).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        progressBar.visibility = View.GONE
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        progressBar.visibility = View.GONE
                        sort.visibility = View.VISIBLE
                        val hospital = p0.getValue(Hospital::class.java)
                        if (listHospitals.size > 0) {
                            var dataExist = false
                            var dataPosition = -1
                            for (i in 0 until listHospitals.size) {
                                if (listHospitals[i].lat == hospital?.lat &&
                                        listHospitals[i].lng == hospital.lng) {
                                    dataExist = true
                                    dataPosition = i
                                }
                            }

                            val emergency = convertHospital(hospital, myLocation)

                            if (!dataExist) {
                                listHospitals.add(emergency)
                            } else {
                                listHospitals[dataPosition] = emergency
                            }

                        } else {
                            val emergency = convertHospital(hospital, myLocation)
                            listHospitals.add(emergency)
                        }

                        mAdapter.notifyDataSetChanged()
                    }

                })
            }
        })


    }

    private fun convertHospital(hospital: Hospital?, myLocation: Location?): Ambulans {
        val results = FloatArray(1)
        Location.distanceBetween(hospital!!.lat, hospital.lng,
                myLocation!!.latitude, myLocation.longitude, results)
        val jarak = String.format("%.2f", results[0] / 1000)
        return Ambulans(hospital.nama, jarak, hospital.nomorTelpon)
    }


    private fun checkLocationSetting() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
        builder.setAlwaysShow(true)


        val task = LocationServices.getSettingsClient(context!!).checkLocationSettings(builder.build())

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
                                    context as AppCompatActivity,
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
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                getDeviceLocation()
            } else {
                // Permission was denied or request was cancelled
            }
        }else if(requestCode == CALL_REQUEST)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //permission call granted
                makePhoneCall()
            }
            else {
                //permission call denied or request was cancelled
            }
        }
    }


    override fun onPause() {
        super.onPause()
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }


}
