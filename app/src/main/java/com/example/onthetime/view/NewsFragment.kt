package com.example.onthetime.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onthetime.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {
    lateinit var binding:FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        return binding.root
    }
}
//Programi acan kimi erroru aradan qaldir.
//Error boyuk ehtimalla gradle filedadir.'
//  App run olmur
