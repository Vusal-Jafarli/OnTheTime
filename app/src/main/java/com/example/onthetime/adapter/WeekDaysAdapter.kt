package com.example.onthetime.adapter

import android.annotation.SuppressLint
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
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class WeekDaysAdapter : ListAdapter<Pair<String, String>, WeekDaysAdapter.DayViewHolder>(DiffCallback()) {


//    val viewModel = CalendarViewModel()


    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.dayTextView2)
        val dayOfWeekTextView: TextView = itemView.findViewById(R.id.weekDayTextView)
        val shiftDay: TextView = itemView.findViewById(R.id.shift_day_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week_day, parent, false)
        return DayViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val (day, dayOfWeek) = getItem(position)
        holder.dayTextView.text = day
        holder.dayOfWeekTextView.text = dayOfWeek.substring(0,3)

        val viewModel = CalendarViewModel()
        holder.dayTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blackTextColor))
        holder.dayOfWeekTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grayTextColor))



        //Burada xeta var , pointMonth deyeri yenilenmediyi ucun viewModel-den alinsa da deger 1 defe alinir ve deyismir buna gore de hemise 9-a beraberdir.Ve buna gorede
//        eger ayin tarixi bu gunun ayin tarixi ile ust uste dusub "Mon" deyerine beraber oldugda textView-un rengi deyisir.
        if(holder.dayOfWeekTextView.text == viewModel.weekDayInit.value && day == viewModel.today.value.toString() && viewModel.toMonth.value == viewModel.pointMonth.value )
        {
            holder.shiftDay.text = "No shift for today."
            holder.shiftDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.lightGrayColor))
            holder.shiftDay.setTypeface(null,Typeface.NORMAL)

            holder.dayTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.mainColor))
            holder.dayTextView.setTypeface(null,Typeface.BOLD)
            holder.dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28f)

            holder.dayOfWeekTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.mainColor))
        }
        else
        { holder.shiftDay.text = "No shift scheduled."
            holder.shiftDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.lightGrayColor))
            holder.shiftDay.setTypeface(null,Typeface.NORMAL)

            holder.dayTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blackTextColor))
            holder.dayTextView.setTypeface(null,Typeface.NORMAL)
            holder.dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26f)

            holder.dayOfWeekTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grayTextColor))

        }



    }

    class DiffCallback : DiffUtil.ItemCallback<Pair<String, String>>() {
        override fun areItemsTheSame(oldItem: Pair<String, String>, newItem: Pair<String, String>): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Pair<String, String>, newItem: Pair<String, String>): Boolean = oldItem == newItem
    }
}
