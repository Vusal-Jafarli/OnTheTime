package com.example.onthetime.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onthetime.databinding.FragmentPendingBinding

class PendingFragment : Fragment()
{
    lateinit var binding:FragmentPendingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding  = FragmentPendingBinding.inflate(inflater,container,false)

        return binding.root
    }


}
