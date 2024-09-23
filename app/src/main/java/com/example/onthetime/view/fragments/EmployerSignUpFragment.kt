package com.example.onthetime.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.example.onthetime.R
import com.example.onthetime.daggerhilt.AuthViewModelFactory
import com.example.onthetime.databinding.FragmentEmployerSignUpBinding
import com.example.onthetime.model.Employer
import com.example.onthetime.repository.AuthRepository
import com.example.onthetime.viewmodel.AuthViewModel
import java.util.UUID

//import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
class EmployerSignUpFragment : Fragment() {
    lateinit var binding: FragmentEmployerSignUpBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var authRepository: AuthRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployerSignUpBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
//        val repository = AuthRepository()
//        viewModel.setRepository(repository)


        authRepository = AuthRepository()

        val factory = AuthViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]



        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_employerSignUpFragment_to_statusFragment)
        }

        binding.signUpButton.setOnClickListener {
            val firstName = binding.employerFirstNameEditText.text.toString()
            val lastName = binding.employerLastNameEditText.text.toString()
            val phoneNumber = binding.employerPhoneNumberEditText.text.toString()
            val email = binding.employerEmailEditText.text.toString()
            val password = binding.employerPasswordEditText.text.toString()
            val uniqueId = UUID.randomUUID().toString()


            val employer = Employer(uniqueId,firstName, lastName, email, phoneNumber, password)

            viewModel.signUp(employer)
        }

        viewModel.signupStatus.observe(viewLifecycleOwner, Observer { status ->
            if (status == true) {
                findNavController().navigate(R.id.action_employerSignUpFragment_to_loginFragment)
            } else {
                binding.noEmailTextview.visibility = View.VISIBLE
            }

        })


        binding.noEmailTextview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val normalDrawable = ContextCompat.getDrawable(
                    requireContext(), R.drawable.login_lock_icon
                )
                binding.noEmailTextview.setCompoundDrawablesWithIntrinsicBounds(
                    normalDrawable, null, null, null
                )

                val hintColor =
                    ContextCompat.getColor(requireContext(), R.color.mainColor) // Yeni hint rengi
                binding.noEmailTextview.setHintTextColor(hintColor)

                val backgroundColor = ContextCompat.getColorStateList(
                    requireContext(), R.color.mainColor
                )
                binding.noEmailTextview.backgroundTintList = backgroundColor

                binding.noEmailTextview.visibility = View.GONE

                val textColor = ContextCompat.getColor(requireContext(), R.color.black)
                binding.noEmailTextview.setTextColor(textColor)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


            return binding.root
    }

}