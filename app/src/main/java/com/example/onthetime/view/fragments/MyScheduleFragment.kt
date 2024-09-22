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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.adapter.DaysAdapter
import com.example.onthetime.adapter.WeekDaysAdapter
import com.example.onthetime.databinding.FragmentMyScheduleBinding
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

        var currentMonth = calendarViewModel.currentMonth.value


        calendarViewModel.loadDaysForMonth(
            calendarViewModel.currentYear.value,
            calendarViewModel.currentMonth.value
        )



        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var year1 = 2024
        var month1 = 9


        if (calendarViewModel.currentMonth.value != null) {
            var month1 = calendarViewModel.currentMonth.value
        }
        if (calendarViewModel.currentYear.value != null) {
            var year1 = calendarViewModel.currentYear.value
        }


        val firstDayOfMonth = LocalDate.of(year1, month1, 1)
        val isLeapYear = firstDayOfMonth.isLeapYear
        val daysInMonth =
            if (calendarViewModel.currentMonth.value == 2 && !isLeapYear) 28 else firstDayOfMonth.lengthOfMonth()


//
//        calendarViewModel.index.observe(viewLifecycleOwner) { index ->
//            calendarViewModel.daysOfWeek.observe(viewLifecycleOwner) { daysOfWeek ->
//                if (index == 2 && daysOfWeek.size == 2 && calendarViewModel.currentMonth.value == 2) {
//                    weekdaysAdapter.submitList(daysOfWeek[index - 1])
//                    adapter.submitList(daysOfWeek[index - 1])
//                    adapter.notifyDataSetChanged()
//                }
//                 else if (index >= 0 && index < daysInMonth) {
//                    weekdaysAdapter.submitList(daysOfWeek[index])
//                    adapter.submitList(daysOfWeek[index])
//                    adapter.notifyDataSetChanged()
//                } else {
//                    Log.e("Error", "Index $index is out of bounds for daysOfWeek size $daysInMonth")
//                }
//            }
//        }

        calendarViewModel.index.observe(viewLifecycleOwner) { index ->
            val daysOfWeek = calendarViewModel.daysOfWeek.value ?: emptyList()
            if (index == 2 && daysOfWeek.size == 2 && calendarViewModel.currentMonth.value == 2) {
                weekdaysAdapter.submitList(daysOfWeek[index - 1])
                adapter.submitList(daysOfWeek[index - 1])
            } else if (index >= 0 && index < daysInMonth) {
                weekdaysAdapter.submitList(daysOfWeek[index])
                adapter.submitList(daysOfWeek[index])
            } else {
                Log.e("Error", "Index $index is out of bounds for daysOfWeek size $daysInMonth")
            }
        }

        binding.leftArrow.setOnClickListener {
            if (calendarViewModel.index.value == 0) {
                previousMonth()
            } else {
                calendarViewModel.previousPage()
            }
//            calendarViewModel.loadDaysForMonth(year1, month1)

        }

        binding.rightArrow.setOnClickListener {
            if (calendarViewModel.index.value == calendarViewModel.daysOfWeek.value?.size?.minus(1)) {
                nextMonth()
                if(calendarViewModel.currentDateDay.value == "31" )
                {
                    calendarViewModel.currentDateDay.value = "1"
                }
                adapter.notifyDataSetChanged()

            } else {
                calendarViewModel.nextPage()

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextMonth() {
        if (calendarViewModel.currentMonth.value == 12) {

            calendarViewModel.currentYear.value = calendarViewModel.currentYear.value!! + 1
            calendarViewModel.currentMonth.value = 1
        } else {
            calendarViewModel.currentMonth.value = calendarViewModel.currentMonth.value!! + 1
        }

        calendarViewModel.loadDaysForMonth(
            calendarViewModel.currentYear.value,
            calendarViewModel.currentMonth.value
        )
        calendarViewModel.index.value = 0

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonth() {
        if (calendarViewModel.currentMonth.value == 1) {
            calendarViewModel.currentYear.value = calendarViewModel.currentYear.value!! - 1
            calendarViewModel.currentMonth.value = 12
        } else {
            calendarViewModel.currentMonth.value = calendarViewModel.currentMonth.value!! - 1
        }

        calendarViewModel.index.value = 0

        calendarViewModel.loadDaysForMonth(
            calendarViewModel.currentYear.value,
            calendarViewModel.currentMonth.value
        )
    }

}