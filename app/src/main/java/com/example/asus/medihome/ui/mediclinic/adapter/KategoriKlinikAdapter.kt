package com.example.asus.medihome.ui.mediclinic.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.asus.medihome.R
import com.example.asus.medihome.model.KategoriKlinik

class KategoriKlinikAdapter(var listKategori : ArrayList<KategoriKlinik>,
                            val clickListener: (KategoriKlinik) -> Unit)
    : RecyclerView.Adapter<KategoriKlinikAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category_clinic, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listKategori.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val kategori = listKategori[position]
        holder.iconKategori.setImageResource(kategori.iconKategoriRes)
        holder.namaKategori.text = kategori.namakategori

        holder.bind(kategori, clickListener)
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconKategori = itemView.findViewById<ImageView>(R.id.category_icon)
        val namaKategori = itemView.findViewById<TextView>(R.id.category_name)

        fun bind(kategori: KategoriKlinik,
                 clickListener: (KategoriKlinik) -> Unit) {
            itemView.setOnClickListener {
                clickListener(kategori)
            }

        }

    }

}