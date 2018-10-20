package com.example.asus.medihome.ui.booking_kamar.pemesanan.metode_pembayaran;

import android.os.Parcel;

import com.thoughtbot.expandablecheckrecyclerview.models.SingleCheckExpandableGroup;

import java.util.List;

public class JenisPembayaran extends SingleCheckExpandableGroup {

    private int iconResId;

    public JenisPembayaran(String title, List items, int iconResId) {
        super(title, items);
        this.iconResId = iconResId;
    }

    protected JenisPembayaran(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JenisPembayaran> CREATOR = new Creator<JenisPembayaran>() {
        @Override
        public JenisPembayaran createFromParcel(Parcel in) {
            return new JenisPembayaran(in);
        }

        @Override
        public JenisPembayaran[] newArray(int size) {
            return new JenisPembayaran[size];
        }
    };
}