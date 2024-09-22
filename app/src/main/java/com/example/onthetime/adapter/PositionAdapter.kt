package com.example.onthetime.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.model.Position

class PositionAdapter(var positions: List<Position>) :
    RecyclerView.Adapter<PositionAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//        val positionColor = view.findViewById<CardView>(R.id.position_color_cardview)
        val positionName = view.findViewById<TextView>(R.id.position_name_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_position,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = positions.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val position = positions[position]
//        val color = ContextCompat.getColor(holder.itemView.context,position.colorCode )

        holder.positionName.text = position.name
//        holder.positionColor.setCardBackgroundColor(color)

    }
}