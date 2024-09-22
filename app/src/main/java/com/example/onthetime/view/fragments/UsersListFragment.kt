package com.example.onthetime.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentUsersListBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class UsersListFragment: Fragment() {

    lateinit var binding:FragmentUsersListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUsersListBinding.inflate(layoutInflater)


        binding.goBackToMessagesButton.setOnClickListener{

            findNavController().navigate(R.id.mainFragment)
//            val bottomNavigationView = view?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
//            bottomNavigationView?.selectedItemId = R.id.messagesFragment
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }
}