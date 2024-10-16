package com.example.onthetime.adapter

import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.viewmodel.CalendarViewModel

class DaysAdapter(var viewModel:CalendarViewModel) : ListAdapter<Pair<String, String>, DaysAdapter.DayViewHolder>(DiffCallback()) {

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)
//        val dayOfWeekTextView: TextView = itemView.findViewById(R.id.weekDayTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val (day, dayOfWeek) = getItem(position)
        holder.dayTextView.text = day

        if (viewModel.mainList.value != null) {

            if (day == viewModel.today.value.toString() && viewModel.toMonth.value == viewModel.pointMonth.value && viewModel.toMonth.value == viewModel.mainList.value?.first()?.month) {
                holder.dayTextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.mainColor
                    )
                )
                holder.dayTextView.setTypeface(null, Typeface.BOLD)
            } else {
                holder.dayTextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.blackTextColor
                    )
                )
                holder.dayTextView.setTypeface(null, Typeface.NORMAL)
            }
        }


    }

    class DiffCallback : DiffUtil.ItemCallback<Pair<String, String>>() {
        override fun areItemsTheSame(
            oldItem: Pair<String, String>,
            newItem: Pair<String, String>
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Pair<String, String>,
            newItem: Pair<String, String>
        ): Boolean = oldItem == newItem
    }
}
