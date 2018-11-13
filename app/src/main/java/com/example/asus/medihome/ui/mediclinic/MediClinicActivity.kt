package com.example.asus.medihome.ui.mediclinic

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import com.example.asus.medihome.R
import com.example.asus.medihome.model.KategoriKlinik
import com.example.asus.medihome.ui.mediclinic.adapter.KategoriKlinikAdapter
import com.example.asus.medihome.ui.mediclinic.dialog.CariClinicDialog
import com.example.asus.medihome.ui.mediclinic.reservasi.PilihJadwalDialog
import com.example.asus.medihome.util.ItemOffsetDecoration
import kotlinx.android.synthetic.main.activity_medi_clinic.*

class MediClinicActivity : AppCompatActivity() {

    lateinit var listKategoriKlinik: ArrayList<KategoriKlinik>
    lateinit var mAdapter: KategoriKlinikAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medi_clinic)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "MediClinic"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listKategoriKlinik = arrayListOf(KategoriKlinik("Gigi", R.drawable.medihome_gigi_icon),
                KategoriKlinik("Kandungan", R.drawable.medihome_kandungan_icon),
                KategoriKlinik("Kulit & Kelamin", R.drawable.medihome_kulit_icon),
                KategoriKlinik("Mata", R.drawable.medihome_mata_icon),
                KategoriKlinik("Pengobatan Herbal", R.drawable.medihome_herbal_icon),
                KategoriKlinik("Psikologi", R.drawable.medihome_psikologi_icon),
                KategoriKlinik("Spa & Kecantikan", R.drawable.medihome_kecantikan_icon),
                KategoriKlinik("THT", R.drawable.medihome_tht_icon),
                KategoriKlinik("Tulang", R.drawable.medihome_tulang_icon))

        setupRecyclerView()

        search_clinic.setOnClickListener {
            showCariKlinikDialog()
        }

    }

    private fun showCariKlinikDialog() {
        val cariClinicDialog = CariClinicDialog()
        cariClinicDialog.show(supportFragmentManager,
                "cari_klinik_dialog_fragment")
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.addItemDecoration(ItemOffsetDecoration(this, R.dimen.itemOffset))
        mAdapter = KategoriKlinikAdapter(listKategoriKlinik) { onCategoryClinicClicked(it) }
        recyclerView.adapter = mAdapter
    }

    private fun onCategoryClinicClicked(kategori: KategoriKlinik) {
        val intent = Intent(this@MediClinicActivity, ListClinicActivity::class.java)
        intent.putExtra("kategoriKlinik", kategori.namakategori)
        startActivity(intent)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }

}
