package com.example.onthetime.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.adapter.MenuItemAdapter
import com.example.onthetime.databinding.FragmentMenuBinding
import com.example.onthetime.viewmodel.MoreViewModel

class MenuFragment : Fragment() {
    lateinit var binding:FragmentMenuBinding

    private lateinit var viewModel:MoreViewModel
    private lateinit var recyclerView:RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MoreViewModel::class.java)

        recyclerView = view.findViewById(R.id.recyclerView)
        var adapter = MenuItemAdapter(emptyList()) { item ->
            Toast.makeText(context, "${item.name} clicked", Toast.LENGTH_SHORT).show()
            if(item.name == "Log Out"){
                findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.menuItems.observe(viewLifecycleOwner) { items ->
            // Update the RecyclerView when data changes
            adapter.items = items
            adapter.notifyDataSetChanged()
        }
    }
}
