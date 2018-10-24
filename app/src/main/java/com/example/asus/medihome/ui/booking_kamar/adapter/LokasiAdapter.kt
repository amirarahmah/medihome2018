package com.example.asus.medihome.ui.booking_kamar.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.asus.medihome.R
import com.example.asus.medihome.R.drawable.hospital
import com.example.asus.medihome.model.Hospital

class LokasiAdapter(var listLokasi : ArrayList<String>,
                      val clickListener: (String) -> Unit)
    : RecyclerView.Adapter<LokasiAdapter.MyViewHolder>() {

    fun filterList(filterdNames: ArrayList<String>) {
        this.listLokasi = filterdNames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_lokasi, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLokasi.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.lokasiTextView.text = listLokasi[position]
        holder.bind(listLokasi[position], clickListener)

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lokasiTextView = itemView.findViewById<TextView>(R.id.nama_lokasi)

        fun bind(lokasi: String,
                 clickListener: (String) -> Unit) {
            itemView.setOnClickListener {
                clickListener(lokasi)
            }

        }

    }

}