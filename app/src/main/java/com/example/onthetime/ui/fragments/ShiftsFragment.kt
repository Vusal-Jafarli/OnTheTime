package com.example.onthetime.ui.fragments

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.onthetime.R
import com.example.onthetime.adapter.ShiftBottomSheet
import com.example.onthetime.databinding.FragmentShiftBinding
import com.example.onthetime.model.Month
import com.example.onthetime.viewmodel.CalendarViewModel
import com.google.android.material.tabs.TabLayout
import java.util.Calendar

class ShiftsFragment : Fragment() {
    lateinit var binding: FragmentShiftBinding
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var calendarViewModel: CalendarViewModel
    lateinit var textViewDate: TextView
    lateinit var calendarButton: Button
    lateinit var moreOptionButton: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShiftBinding.inflate(inflater, container, false)
        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        textViewDate = binding.currentMonthTextViewVusal
        calendarButton = binding.calendarButton
        moreOptionButton = binding.moreOptionButton


        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerTheme,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Month.fromNumber(selectedMonth + 1)
                    textViewDate.text =
                        selectedDate.toString().toLowerCase().replaceFirstChar {
                            it.toTitleCase()
                        }
//                animateView(textViewDate, 1f, 1f)
                    calendarViewModel.loadDays(selectedDay, selectedMonth + 1, selectedYear)
                    calendarViewModel.sendDateToLoadDays()
                },
                year,
                month,
                day
            )
        datePickerDialog.show()

//        val window = datePickerDialog.window
//        if (window != null) {
//            window.setLayout(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT
//            )
//
//            val layoutParams = window.attributes
//            layoutParams.gravity = Gravity.TOP
//            window.attributes = layoutParams
//        }
    }

    private fun animateView(view: View, scaleX: Float, scaleY: Float) {
        val anim = ScaleAnimation(
            view.scaleX, scaleX,
            view.scaleY, scaleY,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim.duration = 300
        anim.fillAfter = true
        view.startAnimation(anim)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarViewModel.today.observe(viewLifecycleOwner) {day ->
            calendarButton.text = day.toString()
        }



        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager

        val adapter = SchedulePagerAdapter(childFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("My Schedule"))
        tabLayout.addTab(tabLayout.newTab().setText("Full Schedule"))
        tabLayout.addTab(tabLayout.newTab().setText("Pending"))

        viewPager2.adapter = adapter

        textViewDate.setOnClickListener {
//            animateView(textViewDate, 1.5f, 1.5f)

            showDatePickerDialog()
        }


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        moreOptionButton.setOnClickListener {
            val bottomSheetFragment = ShiftBottomSheet()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }



    }


}
