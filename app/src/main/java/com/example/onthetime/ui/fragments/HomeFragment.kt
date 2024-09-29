package com.example.onthetime.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentHomeBinding
import com.example.onthetime.viewmodel.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    lateinit var viewModel: LoginViewModel
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val bottomNavigationView = view?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        val navController =
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)

        bottomNavigationView?.setupWithNavController(navController)

        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this


        binding.loginButton.setOnClickListener {
//            Toast.makeText(context, "Login Button Clicked", Toast.LENGTH_SHORT).show()
            viewModel.onLoginClicked()
//            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)

        }
        binding.SignupButton.setOnClickListener {
//            Toast.makeText(context, "Signup Button Clicked", Toast.LENGTH_SHORT).show()
            viewModel.onSignUpClicked()
        }

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                navController.navigate(R.id.action_homeFragment_to_loginFragment)
                viewModel.onNavigateCompleted()
            }

        })


        viewModel.navigateToSignUp.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                navController.navigate(R.id.action_homeFragment_to_statusFragment)
                viewModel.onNavigateCompleted()
            }
        }
        )

        return binding.root
    }
}