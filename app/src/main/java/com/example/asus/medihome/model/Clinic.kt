package com.example.asus.medihome.model

data class Clinic(var cliniclId : String = "", var nama : String = "",
                  var nomorTelpon : String = "", var kota : String = "",
                  var alamatFull : String = "", var jadwal : String = "",
                  var photo : String = "",
                  var lat : Double = 0.0, var lng : Double = 0.0)