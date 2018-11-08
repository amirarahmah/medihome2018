package com.example.asus.medihome.ui.mediclinic.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Layanan

class LayananAdapter(val listLayanan : ArrayList<Layanan>,
                     val clickListener: (Layanan) -> Unit) : RecyclerView.Adapter<LayananAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layanan, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLayanan.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
                .load(listLayanan[position].photo)
                .into(holder.ImageLayanan)

        holder.namaTv.text = listLayanan[position].namaLayanan
        holder.hargaTv.text = listLayanan[position].harga

        holder.bind(listLayanan[position], clickListener)

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ImageLayanan = itemView.findViewById<ImageView>(R.id.image_layanan)
        var namaTv = itemView.findViewById<TextView>(R.id.nama_layanan)
        var hargaTv = itemView.findViewById<TextView>(R.id.harga_layanan)
        var reservasiButton = itemView.findViewById<Button>(R.id.btn_reservasi)

        fun bind(layanan: Layanan,
                 clickListener: (Layanan) -> Unit) {

            reservasiButton.setOnClickListener { clickListener(layanan) }

        }

    }

}