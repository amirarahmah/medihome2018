package com.example.asus.medihome.model

data class Reservation(var idReservation : String = "", var idKlinik : String = "",
                       var idUser : String = "", var namaKlinik : String = "",
                       var alamatKlinik : String = "", var layanan : String = "",
                       var dokter : String = "", var harga : String = "",
                       var tanggal : String = "", var pukul : String = "",
                       var namaPasien : String = "", var jenisKelamin : String = "",
                       var tanggalLahir : String = "", var congirmed : String = "",
                       var payment : String = "")