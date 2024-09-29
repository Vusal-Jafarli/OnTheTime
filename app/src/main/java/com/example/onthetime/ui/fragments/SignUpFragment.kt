package com.example.onthetime.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
        lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    binding = FragmentSignUpBinding.inflate(inflater, container, false)

        val login_textview = view?.findViewById<TextView>(R.id.login_textview)
        login_textview?.setOnClickListener()
        {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.backButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_signUpFragment_to_statusFragment)
        }


        return binding.root

    }

}