package com.example.asus.medihome.ui.mediclinic.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Clinic

class ClinicNearbyAdapter(val listClinics : ArrayList<Clinic>,
                          val clickListener: (Clinic) -> Unit)
    : RecyclerView.Adapter<ClinicNearbyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_clinic, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listClinics.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
                .load(listClinics[position].photo)
                .into(holder.clinicImageView)

        holder.nameTextView.text = listClinics[position].nama

        val lokasi = listClinics[position].alamatFull

        holder.locationTextView.text = lokasi
        holder.jadwalTextView.text = listClinics[position].jadwal

        holder.bind(listClinics[position], clickListener)

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clinicImageView = itemView.findViewById<ImageView>(R.id.klinik_image)
        val nameTextView = itemView.findViewById<TextView>(R.id.nama_klinik_tv)
        val locationTextView = itemView.findViewById<TextView>(R.id.alamat_klinik_tv)
        val jadwalTextView =  itemView.findViewById<TextView>(R.id.jadwal_klinik_tv)

        fun bind(clinic: Clinic,
                 clickListener: (Clinic) -> Unit) {

            itemView.setOnClickListener { clickListener(clinic) }

        }

    }

}