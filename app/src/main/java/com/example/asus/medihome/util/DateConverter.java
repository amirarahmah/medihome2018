package com.example.asus.medihome.util;

public class DateConverter {
    public static String getBulanName(String bulan) {
        if (bulan.equals("01")) {
            return "Januari";
        }
        if (bulan.equals("02")) {
            return "Februari";
        }
        if (bulan.equals("03")) {
            return "Maret";
        }
        if (bulan.equals("04")) {
            return "April";
        }
        if (bulan.equals("05")) {
            return "Mei";
        }
        if (bulan.equals("06")) {
            return "Juni";
        }
        if (bulan.equals("07")) {
            return "Juli";
        }
        if (bulan.equals("08")) {
            return "Augustus";
        }
        if (bulan.equals("09")) {
            return "September";
        }
        if (bulan.equals("10")) {
            return "Oktober";
        }
        if (bulan.equals("11")) {
            return "November";
        }
        if (bulan.equals("12")) {
            return "Desember";
        }
        return "";
    }
}
