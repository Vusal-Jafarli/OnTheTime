package com.example.onthetime.view.fragments

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.gms.common.api.internal.LifecycleFragment


class SchedulePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyScheduleFragment()
            1 -> FullScheduleFragment()
            2 -> PendingFragment()
            else -> MyScheduleFragment()
        }
    }
}