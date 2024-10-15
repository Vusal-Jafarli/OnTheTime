package com.example.onthetime.ui.fragments

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentAddPositionBinding
import com.example.onthetime.model.Position
import com.example.onthetime.viewmodel.PositionsViewModel
import com.google.firebase.auth.FirebaseAuth

class AddPositionFragment : Fragment() {

    lateinit var binding: FragmentAddPositionBinding
    lateinit var viewModel: PositionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(PositionsViewModel::class.java)

        binding = FragmentAddPositionBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelButtonPositions.setOnClickListener {
            findNavController().popBackStack()
        }

        val currentUser = FirebaseAuth.getInstance().currentUser


        var newPositionName = binding.newPositionNameEditText.text
        binding.saveTextView.setOnClickListener {

            var randomNum = (0..10).random()
            var color = R.color.blue
            when (randomNum) {
                1 -> color = R.color.blue
                2 -> color = R.color.darkKhaki
                3 -> color = R.color.lightKhaki
                4 -> color = R.color.khaki
                5 -> color = R.color.darkBlue
                6 -> color = R.color.lightBlue
                else ->  color = R.color.blue

            }

            if (newPositionName != null) {
                if (currentUser != null) {
                    val employerId = currentUser.uid
                    viewModel.addPositionToEmployer(employerId, Position(newPositionName.toString(), color))
                    findNavController().popBackStack()
                }
            }
            else
            {
                Toast.makeText(requireContext(),"Please enter a position name",Toast.LENGTH_SHORT).show()
            }

        }

    }
}