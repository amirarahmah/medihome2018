package com.example.asus.medihome.ui.pesanan


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Pesanan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_pesanan_hospital.*


class PesananHospitalFragment : android.support.v4.app.Fragment() {

    lateinit var mAdapter : PesananHospitalAdapter
    lateinit var pesananList : ArrayList<Pesanan>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pesanan_hospital, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pesananList = arrayListOf()

        setupRecyclerView()
        progressBar?.let { it.visibility = View.VISIBLE }

        val pesananRef = FirebaseDatabase.getInstance().reference.child("pesanan")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        pesananRef.child(userId!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                progressBar?.let { it.visibility = View.INVISIBLE }
            }

            override fun onDataChange(p0: DataSnapshot) {
                progressBar?.let { it.visibility = View.INVISIBLE }
                pesananList.clear()
                for(data in p0.children){
                    val pesanan = data.getValue(Pesanan::class.java)
                    pesananList.add(pesanan!!)
                }

                if(pesananList.size > 0){
                    tidak_ada_reservasi_tv.visibility = View.GONE
                }else{
                    tidak_ada_reservasi_tv.visibility = View.VISIBLE
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
        mAdapter = PesananHospitalAdapter(pesananList)
        recyclerView.adapter = mAdapter
    }

}
