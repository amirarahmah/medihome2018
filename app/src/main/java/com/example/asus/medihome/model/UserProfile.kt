package com.example.asus.medihome.model

data class UserProfile(var userId: String = "", var email: String = "",
                       var nama: String = "", var tanggalLahir: String = "",
                       var alamat : String = "", var jenisKelamin : String = "",
                       var nomorTelpon : String = "", var goldar : String = "",
                       var photoUrl : String = "")