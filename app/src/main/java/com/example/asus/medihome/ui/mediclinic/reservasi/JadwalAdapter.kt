package com.example.asus.medihome.ui.mediclinic.reservasi

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.asus.medihome.R

class JadwalAdapter(var mContext: Context,
                    var listJadwal: ArrayList<String>,
                    val clickListener: (String) -> Unit)
    : RecyclerView.Adapter<JadwalAdapter.MyViewHolder>() {

    private var lastCheckedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_jadwal, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listJadwal.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val jadwal = listJadwal[position]
        holder.jadwalTv.text = jadwal

        if (position == lastCheckedPosition) {
            holder.jadwalTv.background = ContextCompat.getDrawable(mContext, R.drawable.oval_background)
        } else {
            holder.jadwalTv.background = ContextCompat.getDrawable(mContext, R.drawable.oval_background_grey)
        }

        holder.jadwalTv.setOnClickListener {
            lastCheckedPosition = position
            notifyItemRangeChanged(0, itemCount)
            clickListener(jadwal)
        }

        holder.bind(jadwal, clickListener)
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jadwalTv = itemView.findViewById<TextView>(R.id.jadwal_dokter_tv)

        fun bind(jadwal: String,
                 clickListener: (String) -> Unit) {

        }

    }

}