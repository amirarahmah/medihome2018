package com.example.asus.medihome.ui.booking_kamar.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.asus.medihome.R
import com.example.asus.medihome.model.HospitalNearby

class HospitalNearbyAdapter(val listHospital : ArrayList<HospitalNearby>,
                      val clickListener: (HospitalNearby) -> Unit)
    : RecyclerView.Adapter<HospitalNearbyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hospital, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listHospital.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
                .load(listHospital[position].photo)
                .into(holder.hospitalImageView)

        holder.nameTextView.text = listHospital[position].nama


        val lokasi = "${listHospital[position].jarak} Km. " + listHospital[position].alamat

        holder.locationTextView.text = lokasi

        holder.bind(listHospital[position], clickListener)

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalImageView = itemView.findViewById<ImageView>(R.id.hospital_image)
        val nameTextView = itemView.findViewById<TextView>(R.id.hospital_name)
        val typeTextView = itemView.findViewById<TextView>(R.id.hospital_type)
        val locationTextView = itemView.findViewById<TextView>(R.id.hospital_location)

        fun bind(hospital: HospitalNearby,
                 clickListener: (HospitalNearby) -> Unit) {

            itemView.setOnClickListener { clickListener(hospital) }

        }

    }

}