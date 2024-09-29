package com.example.onthetime.ui.fragments

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
import com.example.onthetime.adapter.EmployeesAdapter
import com.example.onthetime.databinding.FragmentEmployeesBinding
import com.example.onthetime.viewmodel.EmployeesViewModel
import com.google.firebase.auth.FirebaseAuth

class EmployeesFragment : Fragment() {
    lateinit var  binding:FragmentEmployeesBinding
    lateinit var recyclerView: RecyclerView
    lateinit var viewModel: EmployeesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goBackToMenuButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addButtonEmployees.setOnClickListener {
            findNavController().navigate(R.id.action_employeesFragment_to_addEmployeeFragment)
        }



        viewModel = ViewModelProvider(this)[EmployeesViewModel::class.java]
        recyclerView = binding.employeesRecyclerview
        var adapter = EmployeesAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val employerId = FirebaseAuth.getInstance().currentUser?.uid

        viewModel.employees.observe(viewLifecycleOwner) { item ->
            if (employerId != null) {
                viewModel.fetchEmployees(employerId)
                binding.progressBarEmployees.visibility = View.GONE
            }
            adapter.employees = item
            binding.countOfEmployees.text = item.size.toString()
            adapter.notifyDataSetChanged()
        }

        viewModel.countOfEmployees.observe(viewLifecycleOwner)
        { item ->
            binding.countOfEmployees.text = item.toString()
        }


    }




}