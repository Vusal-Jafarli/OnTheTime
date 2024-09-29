package com.example.onthetime.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.adapter.LocationsAdapter
import com.example.onthetime.databinding.FragmentLocationsBinding
import com.example.onthetime.viewmodel.LocationsViewModel
import com.google.firebase.auth.FirebaseAuth

class LocationsFragment : Fragment() {
    lateinit var binding: FragmentLocationsBinding
    lateinit var recyclerView: RecyclerView
    lateinit var viewModel: LocationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationsBinding.inflate(layoutInflater)


        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goBackToMenuButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addButtonLocations.setOnClickListener {
            findNavController().navigate(R.id.action_locationsFragment_to_addLocationFragment)
        }

        viewModel = ViewModelProvider(this).get(LocationsViewModel::class.java)
        recyclerView = view.findViewById(R.id.locations_recyclerview)
        var adapter = LocationsAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        val employerId = FirebaseAuth.getInstance().currentUser?.uid

        viewModel.locations.observe(viewLifecycleOwner) { item ->
            if (employerId != null) {
                viewModel.fetchLocations(employerId)
                binding.progressBarLocations.visibility = View.GONE
            }
            adapter.locations = item
            binding.countOfLocations.text = item.size.toString()
            adapter.notifyDataSetChanged()
        }



        viewModel.count_of_locations.observe(viewLifecycleOwner)
        { item ->
            binding.countOfLocations.text = item.toString()
        }

    }
}