package com.example.asus.medihome.ui.info_sehat

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.asus.medihome.R
import com.example.asus.medihome.extension.toast
import com.example.asus.medihome.model.InfoSehat
import com.example.asus.medihome.model.InfoSehatMain
import com.example.asus.medihome.ui.info_sehat_detail.InfoSehatDetailActivity
import com.example.asus.medihome.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_info_sehat.*


class InfoSehatFragment : Fragment(), InfoSehatListener {

    // MARK: - Properties

    // UI
    private var mProgressBar: ProgressBar? = null
    private var mRecyclerView: RecyclerView? = null
    private var mImageViewThumbnail: ImageView? = null
    private var mTextViewTitle: TextView? = null
    private var mTextViewSemua: TextView? = null
    private var mTextViewDietNutrisi: TextView? = null
    private var mTextViewKebugaran: TextView? = null
    private var mTextViewKecantikan: TextView? = null
    private var mTextViewKehamilan: TextView? = null
    private var mTextViewObat: TextView? = null
    private var mTextViewPenyakit: TextView? = null
    private var mTextViewSeksAsmara: TextView? = null
    private var mTextViewTipsSehat: TextView? = null
    private var mInfoSehatMainId: String? = null
    private var mInfoSehatMainTitle: String? = null
    private var mInfoSehatMainImageUrl: String? = null
    private var mInfoSehatMainDescription: String? = null

    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null

    private var FLAG_INFO_SEHAT = 0


    // MARK: - View life cycle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info_sehat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFirebase()
        mProgressBar = view.findViewById(R.id.progressBar)
        mImageViewThumbnail = view.findViewById(R.id.imageview_thumbnail_main)
        mTextViewTitle = view.findViewById(R.id.textview_title_main)
        mTextViewSemua = view.findViewById(R.id.textViewSemua)
        mTextViewDietNutrisi = view.findViewById(R.id.textViewDietNutrisi)
        mTextViewKebugaran = view.findViewById(R.id.textViewKebugaran)
        mTextViewKecantikan = view.findViewById(R.id.textViewKecantikan)
        mTextViewKehamilan = view.findViewById(R.id.textViewKehamilan)
        mTextViewObat = view.findViewById(R.id.textViewObat)
        mTextViewPenyakit = view.findViewById(R.id.textViewPenyakit)
        mTextViewSeksAsmara = view.findViewById(R.id.textViewSeksAsmara)
        mTextViewTipsSehat = view.findViewById(R.id.textViewTipsSehat)

        statusTextView.visibility = View.GONE

        mRecyclerView = view.findViewById(R.id.recyclerView)
        setupRecyclerView()

        fetchInfoSehatItems()
        fetchInfoSehatMain()

        var selectedTextView: TextView? = mTextViewSemua

        mTextViewSemua?.setOnClickListener {
            unselectTextView(selectedTextView)
            selectedTextView = mTextViewSemua
            mTextViewSemua?.setBackgroundDrawable(resources.getDrawable(R.drawable.oval_background_orange))
            cardview.visibility = View.VISIBLE
            fetchInfoSehatItems()
        }

        mTextViewDietNutrisi?.setOnClickListener {
            unselectTextView(selectedTextView)
            selectedTextView = mTextViewDietNutrisi
            mTextViewDietNutrisi?.setBackgroundDrawable(resources.getDrawable(R.drawable.oval_background_orange))
            fetchInfoSehatItems(Constant.CATEGORY.CATEGORY_DIET_NUTRISI)
        }
        mTextViewKebugaran?.setOnClickListener {
            unselectTextView(selectedTextView)
            selectedTextView = mTextViewKebugaran
            mTextViewKebugaran?.setBackgroundDrawable(resources.getDrawable(R.drawable.oval_background_orange))
            fetchInfoSehatItems(Constant.CATEGORY.CATEGORY_KEBUGARAN)
        }
        mTextViewKecantikan?.setOnClickListener {
            unselectTextView(selectedTextView)
            selectedTextView = mTextViewKecantikan
            mTextViewKecantikan?.setBackgroundDrawable(resources.getDrawable(R.drawable.oval_background_orange))
            fetchInfoSehatItems(Constant.CATEGORY.CATEGORY_KECANTIKAN)
        }
        mTextViewKehamilan?.setOnClickListener {
            unselectTextView(selectedTextView)
            selectedTextView = mTextViewKehamilan
            mTextViewKehamilan?.setBackgroundDrawable(resources.getDrawable(R.drawable.oval_background_orange))
            fetchInfoSehatItems(Constant.CATEGORY.CATEGORY_KEHAMILAN)
        }
        mTextViewObat?.setOnClickListener {
            unselectTextView(selectedTextView)
            selectedTextView = mTextViewObat
            mTextViewObat?.setBackgroundDrawable(resources.getDrawable(R.drawable.oval_background_orange))
        }
        mTextViewPenyakit?.setOnClickListener {
            unselectTextView(selectedTextView)
            selectedTextView = mTextViewPenyakit
            mTextViewPenyakit?.setBackgroundDrawable(resources.getDrawable(R.drawable.oval_background_orange))
        }
        mTextViewSeksAsmara?.setOnClickListener {
            unselectTextView(selectedTextView)
            selectedTextView = mTextViewSeksAsmara
            mTextViewSeksAsmara?.setBackgroundDrawable(resources.getDrawable(R.drawable.oval_background_orange))
            fetchInfoSehatItems(Constant.CATEGORY.CATEGORY_SEKS_ASMARA)
        }
        mTextViewTipsSehat?.setOnClickListener {
            unselectTextView(selectedTextView)
            selectedTextView = mTextViewTipsSehat
            mTextViewTipsSehat?.setBackgroundDrawable(resources.getDrawable(R.drawable.oval_background_orange))
            fetchInfoSehatItems(Constant.CATEGORY.CATEGORY_TIPS_SEHAT)
        }

        cardview.setOnClickListener {
            FLAG_INFO_SEHAT = 1
            navigateToInfoSehatDetailActivity(FLAG_INFO_SEHAT, mInfoSehatMainId.toString(), mInfoSehatMainTitle.toString(), mInfoSehatMainImageUrl.toString(), mInfoSehatMainDescription.toString())
        }
    }

    private fun unselectTextView(textView: TextView?) {
        textView?.setBackgroundDrawable(resources.getDrawable(R.drawable.oval_background))
        cardview.visibility = View.GONE
    }


    // MARK: - Networking

    private fun setupFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth?.currentUser
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase?.getReference()
    }

    private var mInfoSehatItems = arrayListOf<InfoSehat>()
    private var mInfoSehatCategory: String? = null

    private fun fetchInfoSehatItems() {
        mInfoSehatItems.clear()
        mDatabaseReference?.child(Constant.CHILD.CHILD_INFOSEHAT)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                context?.toast(p0.message)
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItems: onCancelled: code: ${p0.code}")
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItems: onCancelled: message: ${p0.message}")
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItems: onCancelled: details: ${p0.details}")
                mProgressBar?.visibility = View.INVISIBLE
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItems: onDataChange: dataSnapshot: ${dataSnapshot}")
                if (!dataSnapshot.hasChildren()) {
                    statusTextView.visibility = View.VISIBLE
                    return
                }
                for (childDataSnapshot in dataSnapshot.children) {
                    val infoSehat = childDataSnapshot.getValue(InfoSehat::class.java)
                    mInfoSehatItems.add(infoSehat!!)
                    Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItems: onDataChange: dataSnapshot: mItems.count: ${mInfoSehatItems.count()}")
                }
                if (mInfoSehatItems.isEmpty()) {
                    statusTextView.visibility = View.VISIBLE
                } else {
                    statusTextView.visibility = View.GONE
                }
                mProgressBar?.visibility = View.INVISIBLE
                mAdapter?.notifyDataSetChanged()
            }
        })
    }

    private fun fetchInfoSehatItems(categoryName: String) {
        mInfoSehatItems.clear()
        mDatabaseReference?.child(Constant.CHILD.CHILD_INFOSEHAT)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                context?.toast(p0.message)
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItems: onCancelled: code: ${p0.code}")
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItems: onCancelled: message: ${p0.message}")
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatItems: onCancelled: details: ${p0.details}")
                mProgressBar?.visibility = View.INVISIBLE
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    statusTextView.visibility = View.VISIBLE
                    return
                }
                for (childDataSnapshot in dataSnapshot.children) {
                    val infoSehat = childDataSnapshot.getValue(InfoSehat::class.java)
                    if (infoSehat!!.category == categoryName) {
                        mInfoSehatItems.add(infoSehat)
                    }
                }
                if (mInfoSehatItems.isEmpty()) {
                    statusTextView.visibility = View.VISIBLE
                } else {
                    statusTextView.visibility = View.GONE
                }
                mProgressBar?.visibility = View.INVISIBLE
                mAdapter?.notifyDataSetChanged()
            }
        })
    }

    private fun fetchInfoSehatMain() {
        mDatabaseReference?.child(Constant.CHILD.CHILD_INFOSEHAT_MAIN)?.child("0001")?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                context?.toast(p0.message)
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatMain: onCancelled: code: ${p0.code}")
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatMain: onCancelled: message: ${p0.message}")
                Log.d(Constant.TAG.TAG_INFO_SEHAT, "InfoSehatFragment: fetchInfoSehatMain: onCancelled: details: ${p0.details}")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val infoSehatMain = dataSnapshot.getValue(InfoSehatMain::class.java)
                updateUI(infoSehatMain!!)
            }
        })
    }


    // MARK: - UI Setup

    private var mAdapter: InfoSehatAdapter? = null

    private fun setupRecyclerView() {
        mRecyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mAdapter = InfoSehatAdapter(mInfoSehatItems, this)
        recyclerView.adapter = mAdapter
    }

    private fun updateUI(infosehatmain: InfoSehatMain) {
        Picasso.get().load(infosehatmain.imageUrl).into(mImageViewThumbnail)
        mTextViewTitle?.text = infosehatmain.title
        mInfoSehatMainId = infosehatmain.id
        mInfoSehatMainTitle = infosehatmain.title
        mInfoSehatMainImageUrl = infosehatmain.imageUrl
        mInfoSehatMainDescription = infosehatmain.description
    }


    // MARK: - CategoryListener

    override fun onItemClick(id: String) {
        FLAG_INFO_SEHAT = 2
        navigateToInfoSehatDetailActivity(FLAG_INFO_SEHAT, id)
    }


    // MARK: - Navigation

    private fun navigateToInfoSehatDetailActivity(flag: Int, itemId: String) {
        val intent = Intent(this.context, InfoSehatDetailActivity::class.java)
        intent.putExtra("Flag", flag)
        intent.putExtra(Constant.KEY.KEY_ID_INFO_SEHAT, itemId)
        startActivity(intent)
    }

    private fun navigateToInfoSehatDetailActivity(flag: Int, itemId: String, itemTitle: String, itemImageUrl: String, itemDescription: String) {
        val intent = Intent(this.context, InfoSehatDetailActivity::class.java)
        intent.putExtra("Flag", flag)
        intent.putExtra(Constant.KEY.KEY_ID_INFO_SEHAT, itemId)
        intent.putExtra(Constant.KEY.KEY_TITLE_INFO_SEHAT_MAIN, itemTitle)
        intent.putExtra(Constant.KEY.KEY_IMAGE_URL_INFO_SEHAT_MAIN, itemImageUrl)
        intent.putExtra(Constant.KEY.KEY_DESCRIPTION_INFO_SEHAT_MAIN, itemDescription)
        startActivity(intent)
    }

}
