package com.example.asus.medihome.ui.mediclinic.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.asus.medihome.R
import com.example.asus.medihome.model.KategoriKlinik
import com.example.asus.medihome.model.LayananDetaill

class DetailLayananAdapter(var listDetailLayanan : ArrayList<LayananDetaill>,
                            val clickListener: (LayananDetaill) -> Unit)
    : RecyclerView.Adapter<DetailLayananAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail_layanan, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDetailLayanan.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val layanan = listDetailLayanan[position]
        holder.namaLayananTv.text = layanan.namaLayanan
        holder.hargaLayananTv.text = layanan.harga

        holder.bind(layanan, clickListener)
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaLayananTv = itemView.findViewById<TextView>(R.id.nama_layanan)
        val hargaLayananTv = itemView.findViewById<TextView>(R.id.harga_layanan)

        fun bind(layananDetaill: LayananDetaill,
                 clickListener: (LayananDetaill) -> Unit) {
            itemView.setOnClickListener {
                clickListener(layananDetaill)
            }

        }

    }

}