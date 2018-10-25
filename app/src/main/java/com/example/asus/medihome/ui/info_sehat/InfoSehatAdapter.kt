package com.example.asus.medihome.ui.info_sehat

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.asus.medihome.R
import com.example.asus.medihome.model.InfoSehat
import com.squareup.picasso.Picasso

class InfoSehatAdapter(
        val items: ArrayList<InfoSehat>, val listener: InfoSehatListener
) : RecyclerView.Adapter<InfoSehatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_info_sehat, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description

        val firstPhotoUrl = item.imageUrl
        Picasso.get().load(firstPhotoUrl).into(holder.thumbnailImageView)

        holder.cardView.setOnClickListener {
            listener.onItemClick(item.id)
        }

    }

    override fun getItemCount(): Int {
        return items.count()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        // MARK: - Public Properties
        val cardView: CardView
        val titleTextView: TextView
        val descriptionTextView: TextView
        val thumbnailImageView: ImageView

        // MARK: - Initialization
        init {
            cardView = itemView?.findViewById(R.id.cardview) as CardView
            titleTextView = itemView.findViewById(R.id.textview_title) as TextView
            descriptionTextView = itemView.findViewById(R.id.textview_description) as TextView
            thumbnailImageView = itemView.findViewById(R.id.imageview_thumbnail) as ImageView
        }

    }

}