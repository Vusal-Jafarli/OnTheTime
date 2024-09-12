package com.example.onthetime.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentShiftBinding
import com.example.onthetime.view.fragments.SchedulePagerAdapter
import com.example.onthetime.viewmodel.CalendarViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ShiftsFragment : Fragment() {
    lateinit var binding: FragmentShiftBinding
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShiftBinding.inflate(inflater, container, false)

//        val horizontalRecyclerView = binding.horizontalRecyclerView
//        daysAdapter = DaysAdapter(getDaysOfMonth())
//        horizontalRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        horizontalRecyclerView.adapter = daysAdapter
//
//        binding.leftArrow.setOnClickListener {
//            val currentPosition = (binding.horizontalRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//            if (currentPosition > 0) {
//                binding.horizontalRecyclerView.smoothScrollToPosition(currentPosition - 1)
//            }
//        }
//
//        binding.rightArrow.setOnClickListener {
//            val currentPosition = (binding.horizontalRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//            if (currentPosition < daysAdapter.itemCount - 1) {
//                binding.horizontalRecyclerView.smoothScrollToPosition(currentPosition + 1)
//            }
//        }


        return binding.root
    }

    //    private fun getDaysOfMonth(): List<String> {
//        return (1..30).map { it.toString() }
//    }
//
//    private fun getWeekDays(): List<Pair<String, String>> {
//        return listOf(
//            "1" to "Pazartesi",
//            "2" to "Salı",
//            "3" to "Çarşamba",
//            "4" to "Perşembe",
//            "5" to "Cuma",
//            "6" to "Cumartesi",
//            "7" to "Pazar"
//        )
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager

        val adapter = SchedulePagerAdapter(childFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("My Schedule"))
        tabLayout.addTab(tabLayout.newTab().setText("Full Schedule"))
        tabLayout.addTab(tabLayout.newTab().setText("Pending"))

        viewPager2.adapter = adapter



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
