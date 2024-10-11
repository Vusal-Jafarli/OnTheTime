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
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.onthetime.R
import com.example.onthetime.adapter.ShiftBottomSheet
import com.example.onthetime.databinding.FragmentShiftBinding
import com.example.onthetime.model.Month
import com.example.onthetime.viewmodel.CalendarViewModel
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.util.Calendar

class ShiftsFragment : Fragment() {
    lateinit var binding: FragmentShiftBinding
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    private val calendarViewModel: CalendarViewModel by activityViewModels()
    lateinit var textViewDate: TextView
    lateinit var calendarButton: Button
    lateinit var moreOptionButton: Button
    lateinit var downArrowMonth: ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShiftBinding.inflate(inflater, container, false)
//        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        textViewDate = binding.currentMonthTextViewVusal
        calendarButton = binding.calendarButton
        moreOptionButton = binding.moreOptionButton
        downArrowMonth = binding.downArrowDays

        textViewDate.text = Month.fromNumber(month + 1).toString().toLowerCase().replaceFirstChar {
            it.toTitleCase()
        }


        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarViewModel.today.observe(viewLifecycleOwner) { day ->
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
            showDatePickerDialog()
        }
        downArrowMonth.setOnClickListener{
            showDatePickerDialog()
//            downArrowMonth.setImageResource(R.drawable.down_arrow_icon)
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

        binding.calendarButton.setOnClickListener {
            val today = LocalDate.of(calendarViewModel.thisYear.value!!,calendarViewModel.toMonth.value!!,calendarViewModel.today.value!!)
            val monday = today.with(java.time.DayOfWeek.MONDAY).dayOfMonth

            calendarViewModel.loadDays(monday, calendarViewModel.toMonth.value!!, calendarViewModel.thisYear.value!!)

            val selectedDate = Month.fromNumber(calendarViewModel.toMonth.value!!)
            textViewDate.text =
                selectedDate.toString().toLowerCase().replaceFirstChar {
                    it.toTitleCase()
                }
        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)




        downArrowMonth.setImageResource(R.drawable.up_arrow_white)
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
                    downArrowMonth.setImageResource(R.drawable.down_arrow_icon)

                    val today = LocalDate.of(selectedYear,selectedMonth + 1,selectedDay)
                    val monday = today.with(java.time.DayOfWeek.MONDAY).dayOfMonth


                    calendarViewModel.loadDays(monday, selectedMonth + 1, selectedYear)
                    calendarViewModel.sendDateToLoadDays()
//                    downArrowMonth.setImageResource(R.drawable.down_arrow_icon)

                },
                year,
                month,
                day
            )

        datePickerDialog.show()

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


}
