package com.example.asus.medihome.ui.pesanan

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.asus.medihome.R
import com.example.asus.medihome.model.Reservation
import com.example.asus.medihome.ui.pesanan_detail.PesananDetailActivity
import kotlinx.android.synthetic.main.item_pesanan_clinic.view.*

class PesananClinicAdapter(val listPesanan : ArrayList<Reservation>, val mContext: Context)
    : RecyclerView.Adapter<PesananClinicAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pesanan_clinic, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPesanan.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reservation = listPesanan[position]

        holder.namaKlinikTv.text = reservation.namaKlinik
        holder.namaLayananTv.text = reservation.layanan
        holder.namaDokterTv.text = reservation.dokter
        holder.hargaTv.text = reservation.harga
        holder.idReservasiTv.text = reservation.idReservation
        holder.namaPasienTv.text = reservation.namaPasien
        holder.tanggalTv.text = reservation.tanggal
        holder.pukulTv.text = reservation.pukul
        holder.satusTv.text = "Menunggu Konfirmasi"

        holder.pesananContainer.setOnClickListener {
            val intent = Intent(mContext, PesananDetailActivity::class.java)
            intent.putExtra("idReservation", reservation.idReservation)
            mContext.startActivity(intent)
        }

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val pesananContainer = itemView.pesanan_clinic_container
        val namaKlinikTv = itemView.nama_klinik
        val namaLayananTv = itemView.nama_layanan
        val namaDokterTv = itemView.nama_dokter
        val hargaTv = itemView.harga_layanan
        val idReservasiTv = itemView.id_reservasi_tv
        val namaPasienTv = itemView.nama_pasien_tv
        val tanggalTv = itemView.tanggal_tv
        val pukulTv = itemView.pukul_tv
        val satusTv = itemView.status_tv

    }

}