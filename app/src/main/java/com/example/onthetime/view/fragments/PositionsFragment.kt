package com.example.onthetime.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.adapter.PositionAdapter
import com.example.onthetime.databinding.FragmentPositionsBinding
import com.example.onthetime.viewmodel.PositionsViewModel
import com.google.firebase.auth.FirebaseAuth

class PositionsFragment : Fragment() {

    lateinit var binding: FragmentPositionsBinding
    lateinit var recyclerView: RecyclerView
    lateinit var viewModel: PositionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPositionsBinding.inflate(layoutInflater)


        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goBackToMenuButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addButtonPositions.setOnClickListener {
            findNavController().navigate(R.id.action_positionsFragment_to_addPositionFragment)
        }

        viewModel = ViewModelProvider(this).get(PositionsViewModel::class.java)
        recyclerView = binding.positionsRecyclerview
        var adapter = PositionAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val employerId = FirebaseAuth.getInstance().currentUser?.uid


        viewModel.positions.observe(viewLifecycleOwner)
        { item ->
            if (employerId != null) {
                viewModel.fetchPositions(employerId)
                binding.progressBarPositions.visibility = View.GONE
            }
            adapter.positions = item
            binding.countOfPositions.text = item.size.toString()
            adapter.notifyDataSetChanged()

        }


        viewModel.count_of_positions.observe(viewLifecycleOwner)
        {
            item ->
                binding.countOfPositions.text = item.toString()

        }


    }
}