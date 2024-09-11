package com.example.onthetime.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentStatusBinding

class StatusFragment : Fragment() {
    lateinit var binding: FragmentStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusBinding.inflate(layoutInflater)
        val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
        binding.button.setOnClickListener()
        {
            navController.navigate(R.id.action_statusFragment_to_homeFragment)
        }


        binding.asEmployeeButton.setOnClickListener()
        {
            navController.navigate(R.id.action_statusFragment_to_signUpFragment)
        }

        binding.asEmployerButton.setOnClickListener()
        {
            navController.navigate(R.id.action_statusFragment_to_employerSignUpFragment)
        }


        return binding.root
    }
}