package com.example.onthetime.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.model.Employee
import com.example.onthetime.model.Location

class EmployeesAdapter(var employees: List<Employee>) :
    RecyclerView.Adapter<EmployeesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val employeeName = view.findViewById<TextView>(R.id.employee_name_textview)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_employee,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = employees.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val employee = employees[position]
        holder.employeeName.text = employee.firstName + " " + employee.lastName
    }
}