package com.example.onthetime.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater)


        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        if (savedInstanceState == null) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DashboardFragment()).commit()
        }

        bottomNavigationView.setOnItemSelectedListener { item ->

            val selectedFragment: Fragment = when (item.itemId) {
                R.id.item_shifts -> ShiftsFragment()
                R.id.item_news -> NewsFragment()
                R.id.item_profile -> MenuFragment()
                R.id.item_messages -> MessagesFragment()
                else -> DashboardFragment()
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment).commit()
            true
        }

        return binding.root


    }

}