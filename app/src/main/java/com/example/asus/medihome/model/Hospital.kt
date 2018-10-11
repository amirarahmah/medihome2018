package com.example.asus.medihome.model

data class Hospital(var hospitalId : String = "", var nama : String = "",
                    var nomorTelpon : String = "", var alamat : String = "",
                    var photo : String = "",
                    var lat : Double = 0.0, var lng : Double = 0.0)