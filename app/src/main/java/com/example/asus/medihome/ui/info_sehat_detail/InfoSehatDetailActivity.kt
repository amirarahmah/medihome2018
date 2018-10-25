package com.example.asus.medihome.ui.info_sehat_detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.example.asus.medihome.R
import com.example.asus.medihome.extension.toast
import com.example.asus.medihome.model.InfoSehat
import com.example.asus.medihome.util.Constant
import com.example.asus.medihome.util.Constant.CHILD.CHILD_INFOSEHAT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info_sehat_detail.*

class InfoSehatDetailActivity : AppCompatActivity() {

    private var mExtras: Bundle? = null
    private var mInfoSehatItemId: String? = null
    private var mInfoSehatItemTitle: String? = null
    private var mInfoSehatItemImageUrl: String? = null
    private var mInfoSehatItemDescription: String? = null
    private var flagType: Int = 0

    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_sehat_detail)

        setupFirebase()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mExtras = intent?.extras
        mInfoSehatItemId = mExtras?.getString(Constant.KEY.KEY_ID_INFO_SEHAT)
        mInfoSehatItemTitle = mExtras?.getString(Constant.KEY.KEY_TITLE_INFO_SEHAT_MAIN)
        mInfoSehatItemImageUrl = mExtras?.getString(Constant.KEY.KEY_IMAGE_URL_INFO_SEHAT_MAIN)
        mInfoSehatItemDescription = mExtras?.getString(Constant.KEY.KEY_DESCRIPTION_INFO_SEHAT_MAIN)
        flagType = mExtras?.getInt("Flag")!!
        if (mInfoSehatItemId != null) {
            if (flagType == 2)
                fetchInfoSehatItem(mInfoSehatItemId!!)
            else if (flagType == 1) {
                textview_title.text = mInfoSehatItemTitle
                Picasso.get().load(mInfoSehatItemImageUrl).into(imageview_thumbnail)
                textview_description.text = mInfoSehatItemDescription
                supportActionBar?.title = mInfoSehatItemTitle
            }
        }
    }

    private fun setupFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth?.currentUser
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase?.reference
    }

    private fun fetchInfoSehatItem(id: String) {
        mDatabaseReference?.child(CHILD_INFOSEHAT)?.child(id)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message)
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItem: onCancelled: code: ${p0.code}")
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItem: onCancelled: message: ${p0.message}")
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItem: onCancelled: details: ${p0.details}")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val infoSehatItem = dataSnapshot.getValue(InfoSehat::class.java)
                updateUI(infoSehatItem!!)
            }
        })
    }

    private fun updateUI(infoSehatItem: InfoSehat) {
        supportActionBar?.title = infoSehatItem.title
        Picasso.get().load(infoSehatItem.imageUrl).into(imageview_thumbnail)
        textview_title?.text = infoSehatItem.title.toString()
        textview_description?.text = infoSehatItem.description
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
