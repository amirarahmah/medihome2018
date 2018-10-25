package com.example.asus.medihome.ui.emergency.adapter

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Ambulans
import android.content.Intent
import android.net.Uri


class EmergencyAdapter(var listHospital : ArrayList<Ambulans>,var mContext : Context)
    : RecyclerView.Adapter<EmergencyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_emergency, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listHospital.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hospital = listHospital[position]
        holder.namaRsTv.text = hospital.namaRs
        holder.jarakRsTv.text = "${hospital.jarak} Km"

        holder.callButton.setOnClickListener {
            val phoneNumber = hospital.phone
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
            mContext.startActivity(intent)
        }
    }



    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var namaRsTv = itemView.findViewById<TextView>(R.id.nama_rs_tv)
        val jarakRsTv = itemView.findViewById<TextView>(R.id.jarak_rs_tv)
        val callButton = itemView.findViewById<Button>(R.id.button_panggil)
    }

}