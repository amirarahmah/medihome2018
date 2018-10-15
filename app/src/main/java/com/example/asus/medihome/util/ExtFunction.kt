package com.example.asus.medihome.util

import android.R
import android.widget.ArrayAdapter
import android.widget.Spinner

fun Spinner.setupView(data: List<Any>) {
    val adapter = ArrayAdapter(context,
            R.layout.simple_spinner_item, data)

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    this.adapter = adapter
}
