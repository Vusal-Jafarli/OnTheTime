package com.example.onthetime.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentAddEmployeeBinding

class AddEmployeeFragment : Fragment() {
    lateinit var binding: FragmentAddEmployeeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEmployeeBinding.inflate(layoutInflater)
        return binding.root
    }
}

