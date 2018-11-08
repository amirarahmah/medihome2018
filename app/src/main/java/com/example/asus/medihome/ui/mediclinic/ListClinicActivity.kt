package com.example.asus.medihome.ui.mediclinic

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.example.asus.medihome.R
import com.example.asus.medihome.R.drawable.hospital
import com.example.asus.medihome.model.Clinic
import com.example.asus.medihome.ui.booking_kamar.dialog.SortingDialog
import com.example.asus.medihome.ui.booking_kamar.profile_rs.ProfilRumahSakitActivity
import com.example.asus.medihome.ui.mediclinic.adapter.ClinicNearbyAdapter
import com.example.asus.medihome.ui.mediclinic.dialog.SortingClinicDialog
import com.example.asus.medihome.ui.mediclinic.profile.ProfileClinicActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_list_clinic.*

class ListClinicActivity : AppCompatActivity() {

    val REQUEST_LOCATION = 900
    val REQUEST_CHECK_SETTINGS = 100

    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    var listClinics: ArrayList<Clinic> = arrayListOf()
    lateinit var mAdapter: ClinicNearbyAdapter

    lateinit var clinicRef: DatabaseReference

    var kategoriKlinik : String? = ""
    var alamatKlinik : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_clinic)

        kategoriKlinik = intent?.extras?.getString("kategoriKlinik")

        supportActionBar?.title = "Klinik $kategoriKlinik"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        clinicRef = FirebaseDatabase.getInstance().reference.child("clinic")

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                val location = locationResult.lastLocation
                getNearbyClinic(location)
                mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
            }
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setupRecyclerView()
        checkLocationSetting()

        sort.setOnClickListener {
            showSortingDialog()
        }

    }

    private fun getNearbyClinic(location: Location?) {
        clinicRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                progressBar.visibility = View.GONE
            }
            override fun onDataChange(p0: DataSnapshot) {
                progressBar.visibility = View.GONE
                listClinics.clear()
                for (data in p0.children) {
                    val clinicId = data.child("hospitalId").value.toString()
                    val nama = data.child("nama").value.toString()
                    val nomorTelpon = data.child("nomorTelpon").value.toString()
                    val kota = data.child("kota").value.toString()
                    val jadwal = data.child("jadwal").value.toString()
                    val photo = data.child("photo").value.toString()
                    val lat = data.child("lat").value.toString().toDouble()
                    val lng= data.child("lng").value.toString().toDouble()
                    alamatKlinik = data.child("alamatFull").value.toString()

                    val results = FloatArray(1)
                    Location.distanceBetween(lat, lng,
                           location!!.latitude, location.longitude, results)
                    val jarak = String.format("%.2f", results[0] / 1000)

                    val lokasi = "$jarak km. $alamatKlinik"

                    val clinic = Clinic(clinicId, nama, nomorTelpon, kota, lokasi
                    ,jadwal, photo, lat, lng)

                    listClinics.add(clinic)

                }

                mAdapter.notifyDataSetChanged()

            }
        })
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = ClinicNearbyAdapter(listClinics) { clinic: Clinic -> clinicClicked(clinic) }
        recyclerView.adapter = mAdapter
    }


    private fun clinicClicked(clinic: Clinic) {
        val intent = Intent(this, ProfileClinicActivity::class.java)
        intent.putExtra("hospitalId", clinic.cliniclId)
        intent.putExtra("nama", clinic.nama)
        intent.putExtra("kategoriKlinik", kategoriKlinik)
        intent.putExtra("nomorTelpon", clinic.nomorTelpon)
        intent.putExtra("alamat", alamatKlinik)
        intent.putExtra("jadwal", clinic.jadwal)
        intent.putExtra("lat", clinic.lat)
        intent.putExtra("lng", clinic.lng)
        intent.putExtra("photo", clinic.photo)
        startActivity(intent)
    }


    private fun showSortingDialog() {
        val sortingDialog = SortingClinicDialog.newInstance()
        val fragmentManager = supportFragmentManager
        sortingDialog.show(fragmentManager, "sorting_fragment")
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


    private fun getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION)
        } else {
            progressBar.visibility = View.VISIBLE
            //permission granted
            mFusedLocationProviderClient.lastLocation
                    .addOnSuccessListener(this) { location ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            getNearbyClinic(location)
                        } else {
                            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, null)
                        }
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
        }
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
