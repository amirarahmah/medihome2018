package com.example.asus.medihome.model

data class HospitalNearby(var hospitalId : String = "", var nama : String = "",
                    var nomorTelpon : String = "", var kota : String = "",
                    var alamat : String = "", var alamatFull : String = "",
                    var photo : String = "", var jarak : String = "",
                    var lat : Double = 0.0, var lng : Double = 0.0)