package com.example.onthetime.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentShiftBinding
import com.example.onthetime.viewmodel.CalendarViewModel
import com.google.android.material.tabs.TabLayout

class ShiftsFragment : Fragment() {
    lateinit var binding: FragmentShiftBinding
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShiftBinding.inflate(inflater, container, false)

        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

//        val currentMontTextView = view?.findViewById<TextView>(R.id.currentMonthTextViewVusal)
//
//        calendarViewModel.month.observe(viewLifecycleOwner)
//        { item ->
//            currentMontTextView?.text = item.toString()
//
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager

        val adapter = SchedulePagerAdapter(childFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("My Schedule"))
        tabLayout.addTab(tabLayout.newTab().setText("Full Schedule"))
        tabLayout.addTab(tabLayout.newTab().setText("Pending"))

        viewPager2.adapter = adapter


        val currentMontTextView = view?.findViewById<TextView>(R.id.currentMonthTextViewVusal)
        calendarViewModel.month.observe(viewLifecycleOwner)
        { item ->
            currentMontTextView?.text = item.toString()
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

    }


}
