package com.example.onthetime.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onthetime.databinding.FragmentAddLocationBinding
import com.example.onthetime.model.Location
import com.example.onthetime.viewmodel.LocationsViewModel
import com.google.firebase.auth.FirebaseAuth

class AddLocationFragment : Fragment() {

    lateinit var binding: FragmentAddLocationBinding
    lateinit var viewModel: LocationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LocationsViewModel::class.java)
        binding = FragmentAddLocationBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelButtonLocations.setOnClickListener {
            findNavController().popBackStack()
        }

        val currentUser = FirebaseAuth.getInstance().currentUser

        var newLocationName = binding.newLocationNameEditText

        binding.saveTextView.setOnClickListener {
            if (newLocationName != null) {
                if (currentUser != null) {
                    val employerId = currentUser.uid
                    viewModel.addLocationToEmployer(employerId, Location(newLocationName.text.toString(), ""))
                    findNavController().popBackStack()
                }
            } else
                Toast.makeText(
                    requireContext(), "Please enter a location name",
                    Toast.LENGTH_SHORT
                ).show()


        }

    }
}