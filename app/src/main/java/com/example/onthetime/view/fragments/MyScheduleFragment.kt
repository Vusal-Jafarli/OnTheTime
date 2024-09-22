package com.example.onthetime.view.fragments

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.adapter.DaysAdapter
import com.example.onthetime.adapter.WeekDaysAdapter
import com.example.onthetime.databinding.FragmentMyScheduleBinding
import com.example.onthetime.databinding.FragmentShiftBinding
import com.example.onthetime.viewmodel.CalendarViewModel
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*

class MyScheduleFragment : Fragment() {

    lateinit var binding: FragmentMyScheduleBinding
    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var adapter: DaysAdapter
    private lateinit var weekdaysAdapter: WeekDaysAdapter


    private var bugunku_gun = "19"
    private var bugunku_ay = "September"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentMyScheduleBinding.inflate(inflater, container, false)

        calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]
        adapter = DaysAdapter()
        weekdaysAdapter = WeekDaysAdapter()
        var currentDayTextView = view?.findViewById<TextView>(R.id.dayTextView2)
        var currentWeekDayTextView = view?.findViewById<TextView>(R.id.weekDayTextView)

        val recyclerView = binding.horizontalRecyclerView
        val verticalRecyclerView = binding.verticalRecyclerView


        recyclerView.adapter = adapter
        verticalRecyclerView.adapter = weekdaysAdapter

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        verticalRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

//        var currentMonth = calendarViewModel.currentMonth.value
        var totalHoursTextView = binding.totalHoursTextView
        var periodTextView = binding.periodTextView


        calendarViewModel.loadDays(22, 9, 2024)

        calendarViewModel.daysOfWeek.observe(viewLifecycleOwner)
        { daysOfWeek ->
            adapter.submitList(daysOfWeek)
            weekdaysAdapter.submitList(daysOfWeek)
        }



        calendarViewModel.mainList.observe(viewLifecycleOwner)
        { mainList ->
            var periodText =
                mainList.first().day.toString() + " " + com.example.onthetime.model.Month.fromNumber(
                    mainList.first().month
                ).toString().substring(
                    0,
                    3
                ).toLowerCase()
                    .replaceFirstChar { it.uppercase() } + " - " + mainList.last().day.toString() + " " + com.example.onthetime.model.Month.fromNumber(
                    mainList.last().month
                ).toString().substring(0, 3).toLowerCase().replaceFirstChar { it.uppercase() }
            periodTextView.text = periodText

            totalHoursTextView.text = "0 hours"

        }


        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.leftArrow.setOnClickListener {
            //Not yet
        }
        binding.rightArrow.setOnClickListener {
            var day = calendarViewModel.mainList.value!!.last().day
            var month = calendarViewModel.mainList.value!!.last().month
            var year = calendarViewModel.mainList.value!!.last().year

            val date = LocalDate.of(year, month, day)
            val lengthOfMonth = date.lengthOfMonth()

            if (lengthOfMonth != day) {
                day += 1
            } else {
                if (month == 12) {
                    month = 1
                    year = year + 1
                    day = 1
                } else {
                    month = month + 1
                    year = year
                    day = 1
                }
            }

            calendarViewModel.loadDays(day, month, year)
        }


    }
}
