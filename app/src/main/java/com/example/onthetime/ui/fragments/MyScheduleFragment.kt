package com.example.onthetime.ui.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onthetime.R
import com.example.onthetime.adapter.DaysAdapter
import com.example.onthetime.adapter.FullScheduleRecyclerViewAdapter
import com.example.onthetime.adapter.WeekDaysAdapter
import com.example.onthetime.databinding.FragmentMyScheduleBinding
import com.example.onthetime.model.Shift
import com.example.onthetime.viewmodel.CalendarViewModel
import java.time.LocalDate

class MyScheduleFragment : Fragment() {

    lateinit var binding: FragmentMyScheduleBinding
    private lateinit var adapter: DaysAdapter
    private lateinit var weekdaysAdapter: WeekDaysAdapter
    private lateinit var fullScheduleRecyclerViewAdapter: FullScheduleRecyclerViewAdapter
    private val calendarViewModel: CalendarViewModel by activityViewModels()


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var sharedPreferences =
            requireContext().getSharedPreferences("user_data", MODE_PRIVATE)
        val status = sharedPreferences.getString("status", "")

        binding = FragmentMyScheduleBinding.inflate(inflater, container, false)

        val today = LocalDate.now()
        val monday = today.with(java.time.DayOfWeek.MONDAY).dayOfMonth
        val thisMonth  = LocalDate.now().monthValue

        calendarViewModel.loadDays(monday, thisMonth, calendarViewModel.thisYear.value!!)

        adapter = DaysAdapter(calendarViewModel)
        weekdaysAdapter = WeekDaysAdapter(calendarViewModel)
        fullScheduleRecyclerViewAdapter = FullScheduleRecyclerViewAdapter(calendarViewModel,requireContext(),emptyList<Pair<Pair<String, String>, List<Shift>>>()){
            shift ->
            val bundle = Bundle().apply {
                putParcelable("shift", shift)
            }
            findNavController().navigate(R.id.shiftDetailsFragment, bundle)}

        val recyclerView = binding.horizontalRecyclerView
        val verticalRecyclerView = binding.verticalRecyclerView


        recyclerView.adapter = adapter
        verticalRecyclerView.adapter = fullScheduleRecyclerViewAdapter

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        verticalRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        var totalHoursTextView = binding.totalHoursTextView
        var periodTextView = binding.periodTextView


        if(status == "employer"){
        calendarViewModel.daysOfWeek.observe(viewLifecycleOwner)
        { daysOfWeek ->
            adapter.submitList(daysOfWeek)
            weekdaysAdapter.submitList(daysOfWeek)
            verticalRecyclerView.adapter  = weekdaysAdapter
        }}
        else if(status == "employee"){
            calendarViewModel.daysOfWeek.observe(viewLifecycleOwner)
            { daysOfWeek ->
                adapter.submitList(daysOfWeek)

                calendarViewModel.daysAndShiftsForEmployee.observe(viewLifecycleOwner){
                        list ->
                   var a =  FullScheduleRecyclerViewAdapter(calendarViewModel,requireContext(),
                        list,
                        onShiftClick = { shift ->
                            val bundle = Bundle().apply {
                                putParcelable("shift", shift)
                            }
                            findNavController().navigate(R.id.shiftDetailsFragment, bundle)
                        }
                    )
                    verticalRecyclerView.adapter = a
                    a.notifyDataSetChanged()
                }


            }
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
            var day = calendarViewModel.mainList.value!!.first().day
            var month = calendarViewModel.mainList.value!!.first().month
            var year = calendarViewModel.mainList.value!!.first().year



            if (day > 7) {
                day = calendarViewModel.mainList.value!!.first().day - 7
            } else {
                var daysNeeded = 7 - day

                if (month == 1) {
                    month = 12
                    year = year - 1


                    val date = LocalDate.of(year, month, day)
                    val lengthOfMonth = date.lengthOfMonth()

                    day = lengthOfMonth - daysNeeded


                } else {
                    month = calendarViewModel.mainList.value!!.first().month - 1
                    day = calendarViewModel.mainList.value!!.first().day

                    val date = LocalDate.of(year, month, day)
                    val lengthOfMonth = date.lengthOfMonth()

                    day = lengthOfMonth - daysNeeded
                }
            }

            calendarViewModel.loadDays(day, month, year)

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
