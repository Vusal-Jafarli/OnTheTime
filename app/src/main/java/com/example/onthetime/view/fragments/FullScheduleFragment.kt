package com.example.onthetime.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onthetime.databinding.FragmentFullScheduleBinding

class FullScheduleFragment: Fragment() {

    lateinit var binding:FragmentFullScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFullScheduleBinding.inflate(inflater,container,false)


        return binding.root
    }
}