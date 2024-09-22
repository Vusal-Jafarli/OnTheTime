package com.example.onthetime.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.model.MenuItem

// MenuItemAdapter.kt
class MenuItemAdapter(
    public var items: List<MenuItem>,
    public var itemClickListener: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_more_recyclerview, parent, false)
        return MenuItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int = items.size

    class MenuItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val arrowImageView: ImageView = itemView.findViewById(R.id.arrowImageView)

        fun bind(item: MenuItem, clickListener: (MenuItem) -> Unit) {
            iconImageView.setImageResource(item.iconResId)
            nameTextView.text = item.name
            itemView.setOnClickListener { clickListener(item) }
        }
    }
}
