package com.example.asus.medihome.ui.mediclinic.dialog

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome.R
import com.example.asus.medihome.R.string.lokasi
import com.example.asus.medihome.model.Clinic
import com.example.asus.medihome.ui.mediclinic.adapter.ClinicAdapter
import com.example.asus.medihome.ui.mediclinic.adapter.DetailLayananAdapter
import com.example.asus.medihome.ui.mediclinic.profile.ProfileClinicActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.dialog_cari_clinic.*

class CariClinicDialog : DialogFragment() {

    lateinit var mAdapter: ClinicAdapter
    var listClinics: ArrayList<Clinic> = arrayListOf()

    lateinit var clinicRef: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dialog_cari_clinic, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clinicRef = FirebaseDatabase.getInstance().reference.child("clinic")

        button_close.setOnClickListener {
            dialog.dismiss()
        }

        setupRecyclerView()
        fetchAllClinic()

        search_lokasi.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(listClinics.size > 0){
                    if(p0.toString().length > 3){
                        filterClinic(p0.toString())
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })


    }

    private fun filterClinic(text: String) {
        val filteredList = arrayListOf<Clinic>()
        for(clinic in listClinics){
            if (clinic.nama.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(clinic)
            }
        }

        mAdapter.filterList(filteredList)
    }

    private fun fetchAllClinic() {
        clinicRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
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
                    val alamatKlinik = data.child("alamatFull").value.toString()


                    val clinic = Clinic(clinicId, nama, nomorTelpon, kota, alamatKlinik
                            ,jadwal, photo, lat, lng)

                    listClinics.add(clinic)

                }

            }
        })
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = ClinicAdapter(listClinics) { clinicClicked(it) }
        recyclerView.adapter = mAdapter
    }


    private fun clinicClicked(clinic: Clinic) {
        val intent = Intent(context, ProfileClinicActivity::class.java)
        intent.putExtra("idKlinik", clinic.cliniclId)
        intent.putExtra("nama", clinic.nama)
        intent.putExtra("kategoriKlinik", "")
        intent.putExtra("nomorTelpon", clinic.nomorTelpon)
        intent.putExtra("alamat", clinic.alamatFull)
        intent.putExtra("jadwal", clinic.jadwal)
        intent.putExtra("lat", clinic.lat)
        intent.putExtra("lng", clinic.lng)
        intent.putExtra("photo", clinic.photo)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setWindowAnimations(R.style.DialogAnimation)
            dialog.window!!.setLayout(width, height)
        }
    }

    companion object {

        fun newInstance(): CariClinicDialog {
            return CariClinicDialog()
        }
    }

}