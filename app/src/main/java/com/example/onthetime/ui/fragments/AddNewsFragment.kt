package com.example.onthetime.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onthetime.databinding.FragmentAddNewsBinding
import com.example.onthetime.model.News
import com.example.onthetime.viewmodel.NewsViewModel
import com.google.firebase.auth.FirebaseAuth

class AddNewsFragment : Fragment() {
    lateinit var binding: FragmentAddNewsBinding
    lateinit var newsViewModel: NewsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewsBinding.inflate(inflater, container, false)
        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelButtonNews.setOnClickListener {
            findNavController().popBackStack()
        }

        val currentUser = FirebaseAuth.getInstance().currentUser


        var newPositionName = binding.newPostNameEditText.text
        binding.saveTextView.setOnClickListener {

            if (newPositionName != null) {
                if (currentUser != null) {
                    val employerId = currentUser.uid
                    newsViewModel.addNewsToEmployer(employerId, News(currentUser.uid,newPositionName.toString()))
                    findNavController().popBackStack()
                }
            }
            else
            {
                Toast.makeText(requireContext(),"Please enter any message", Toast.LENGTH_SHORT).show()
            }

        }


    }
}