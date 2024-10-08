package com.example.onthetime.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onthetime.R
import com.example.onthetime.daggerhilt.AuthViewModelFactory
import com.example.onthetime.databinding.FragmentLoginBinding
import com.example.onthetime.repository.AuthRepository
import com.example.onthetime.viewmodel.AuthViewModel

//import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var viewModel: AuthViewModel
    lateinit var binding: FragmentLoginBinding
    private lateinit var authRepository: AuthRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
//        viewmodel = ViewModelProvider(this).get(AuthViewModel::class.java)
//        val repository = AuthRepository()

//        viewmodel.setRepository(repository)


        authRepository = AuthRepository()

        val factory = AuthViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]


        val signUpTextView = binding.signUpTextview
        val emailEditText: EditText = binding.emailEdittext
        val passwordEditText: EditText = binding.passwordEdittext

        binding.loginButtonMain.setOnClickListener() {

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            viewModel.login(email, password)

        }

        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { status ->
            if (status == true) {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            } else {
                binding.wrongPasswordTextview.visibility = View.VISIBLE

                val hintColor =
                    ContextCompat.getColor(requireContext(), R.color.red) // Yeni hint rengi
                binding.passwordEdittext.setHintTextColor(hintColor)

                val backgroundColor = ContextCompat.getColorStateList(
                    requireContext(), R.color.red
                ) // Yeni arka plan rengi
                binding.passwordEdittext.backgroundTintList = backgroundColor

                val errorDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.login_lock_icon_red)
                binding.passwordEdittext.setCompoundDrawablesWithIntrinsicBounds(
                    errorDrawable, null, null, null
                )


                val textColor = ContextCompat.getColor(requireContext(), R.color.red)
                binding.passwordEdittext.setTextColor(textColor)


            }
        })

        binding.passwordEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val normalDrawable = ContextCompat.getDrawable(
                    requireContext(), R.drawable.login_lock_icon
                )
                passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                    normalDrawable, null, null, null
                )

                val hintColor =
                    ContextCompat.getColor(requireContext(), R.color.mainColor) // Yeni hint rengi
                binding.passwordEdittext.setHintTextColor(hintColor)

                val backgroundColor = ContextCompat.getColorStateList(
                    requireContext(), R.color.mainColor
                ) // Yeni arka plan rengi
                binding.passwordEdittext.backgroundTintList = backgroundColor

                binding.wrongPasswordTextview.visibility = View.GONE

                val textColor = ContextCompat.getColor(requireContext(), R.color.black)
                binding.passwordEdittext.setTextColor(textColor)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        signUpTextView?.setOnClickListener() {
            findNavController().navigate(R.id.action_loginFragment_to_statusFragment)

        }


        return binding.root

    }
}
