package com.example.onthetime.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.model.Location

class LocationsAdapter(var locations: List<Location>) :
    RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val locationName = view.findViewById<TextView>(R.id.location_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = locations.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val location = locations[position]
        holder.locationName.text = location.name
    }
}
