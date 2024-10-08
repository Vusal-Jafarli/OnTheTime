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
import com.example.onthetime.model.Date
import com.example.onthetime.model.News
import com.example.onthetime.model.Time
import com.example.onthetime.viewmodel.NewsViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

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
        var userName: String? = null
        var userSurname: String? = null
        newsViewModel.repository.getEmployerName(currentUser!!.uid) { name ->
            userName = name
        }
        newsViewModel.repository.getEmployerLastName(currentUser!!.uid) { surname ->
            userSurname = surname
        }

        var newPositionName = binding.newPostNameEditText.text
        binding.saveTextView.setOnClickListener {

            if (newPositionName != null) {
                if (currentUser != null) {
                    val employerId = currentUser.uid
                    val author = userName + " " + userSurname
                    val calendar = Calendar.getInstance()

                    val hour = calendar.get(Calendar.HOUR_OF_DAY) // For 24-hour format
                    val minute = calendar.get(Calendar.MINUTE)
                    val second = calendar.get(Calendar.SECOND)
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    val time: Time = Time(hour, minute, second)
                    val date: Date = Date(year, month + 1, day)

                    newsViewModel.addNewsToEmployer(
                        employerId,
                        News(
                            currentUser.uid,
                            authornameSurname = author,
                            time = time,
                            date = date,
                            message = newPositionName.toString()
                        ),
                    )
                    findNavController().popBackStack()
                }
            } else {
                Toast.makeText(requireContext(), "Please enter any message", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }
}