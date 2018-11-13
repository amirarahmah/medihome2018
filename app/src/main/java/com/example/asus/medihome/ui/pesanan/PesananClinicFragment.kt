package com.example.asus.medihome.ui.pesanan


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Reservation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_pesanan_hospital.*


class PesananClinicFragment : Fragment() {

    lateinit var mAdapter: PesananClinicAdapter
    lateinit var pesananList: ArrayList<Reservation>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pesanan_clinic, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pesananList = arrayListOf()

        setupRecyclerView()
        progressBar?.let { it.visibility = View.VISIBLE }

        val reservasinRef = FirebaseDatabase.getInstance().reference.child("reservasi")
        val idUser = FirebaseAuth.getInstance().currentUser?.uid

        reservasinRef.orderByChild("idUser").equalTo(idUser!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        progressBar?.let { it.visibility = View.INVISIBLE }
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        progressBar?.let { it.visibility = View.INVISIBLE }
                        pesananList.clear()
                        for (data in p0.children) {
                            val pesanan = data.getValue(Reservation::class.java)
                            pesananList.add(pesanan!!)
                        }
                        mAdapter.notifyDataSetChanged()
                    }
                })

    }


    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager
        mAdapter = PesananClinicAdapter(pesananList, context!!)
        recyclerView.adapter = mAdapter
    }

}
