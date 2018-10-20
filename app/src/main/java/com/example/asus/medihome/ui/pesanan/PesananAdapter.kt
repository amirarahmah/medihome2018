package com.example.asus.medihome.ui.pesanan

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Pesanan

class PesananAdapter(val listPesanan : ArrayList<Pesanan>) : RecyclerView.Adapter<PesananAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pesanan, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPesanan.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pesanan = listPesanan[position]

        holder.namaRumahSakitTv.text = pesanan.namaRumahSakit
        holder.tipeKamarTv.text = pesanan.jenisKamar
        holder.namaPasienTv.text = pesanan.namaPasien
        holder.checkInTv.text = pesanan.tanggalCheckIn
        holder.idPesananTv.text = pesanan.idPesanan
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var namaRumahSakitTv = itemView.findViewById<TextView>(R.id.nama_rs_tv)
        var tipeKamarTv = itemView.findViewById<TextView>(R.id.tipe_kama_tv)
        var checkInTv = itemView.findViewById<TextView>(R.id.check_in_tv)
        var namaPasienTv = itemView.findViewById<TextView>(R.id.nama_pasien_tv)
        var idPesananTv = itemView.findViewById<TextView>(R.id.id_pesanan_tv)

    }

}