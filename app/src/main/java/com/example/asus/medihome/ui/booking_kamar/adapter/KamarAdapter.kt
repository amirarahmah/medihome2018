package com.example.asus.medihome.ui.booking_kamar.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Kamar

class KamarAdapter(val listKamar : ArrayList<Kamar>,
                   val clickListener: (Kamar) -> Unit,
                   val clickListener2 : (Kamar) -> Unit) : RecyclerView.Adapter<KamarAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_kamar, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listKamar.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
                .load(listKamar.get(position).photo)
                .into(holder.ImageKamar)

        holder.jenisKamarTv.text = listKamar[position].jenisKamar
        holder.jenisKasurTv.text = listKamar[position].jenisKasur
        holder.hargaTv.text = listKamar[position].harga
        holder.kamarTersisaTv.text = listKamar[position].jumlahKamar.toString()

        holder.bind(listKamar[position], clickListener, clickListener2)

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var kamarCardView = itemView.findViewById<CardView>(R.id.item_kamar)
        var ImageKamar = itemView.findViewById<ImageView>(R.id.image_kamar)
        var jenisKamarTv = itemView.findViewById<TextView>(R.id.jenis_kamar)
        var jenisKasurTv = itemView.findViewById<TextView>(R.id.jenis_kasur)
        var hargaTv = itemView.findViewById<TextView>(R.id.kamar_price)
        var kamarTersisaTv = itemView.findViewById<TextView>(R.id.kamar_tersisa)
        var pesanButton = itemView.findViewById<Button>(R.id.btn_pesan)

        fun bind(kamar: Kamar,
                 clickListener: (Kamar) -> Unit,
                 clickListener2: (Kamar) -> Unit) {

            pesanButton.setOnClickListener { clickListener(kamar) }
            kamarCardView.setOnClickListener { clickListener2(kamar) }

        }

    }

}