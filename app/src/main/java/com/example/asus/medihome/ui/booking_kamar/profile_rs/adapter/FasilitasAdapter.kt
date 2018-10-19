package com.example.asus.medihome.ui.booking_kamar.profile_rs.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Fasilitas
import com.example.asus.medihome.model.Hospital


class FasilitasAdapter(val listFasilitas : ArrayList<Fasilitas>)
    : RecyclerView.Adapter<FasilitasAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_fasilitas, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFasilitas.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val fasilitas = listFasilitas[position]
        holder.fasilitasImage.setImageResource(fasilitas.imageFasilitas)
        holder.fasilitasName.text = fasilitas.namaFasilitas
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fasilitasImage = itemView.findViewById<ImageView>(R.id.image_fasilitas)
        val fasilitasName = itemView.findViewById<TextView>(R.id.nama_fasilitas)
    }

}