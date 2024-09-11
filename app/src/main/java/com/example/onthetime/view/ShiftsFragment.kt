package com.example.onthetime.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onthetime.databinding.FragmentShiftBinding

class ShiftsFragment : Fragment() {
lateinit var binding:FragmentShiftBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShiftBinding.inflate(inflater, container, false)

        return binding.root
    }
}
