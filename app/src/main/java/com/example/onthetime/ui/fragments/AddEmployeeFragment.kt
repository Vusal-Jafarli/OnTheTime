package com.example.onthetime.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentAddEmployeeBinding
import com.example.onthetime.model.Employee
import com.example.onthetime.model.Location
import com.example.onthetime.model.Position
import com.example.onthetime.viewmodel.EmployeesViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class AddEmployeeFragment : Fragment() {
    lateinit var binding: FragmentAddEmployeeBinding
    lateinit var viewModel: EmployeesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEmployeeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[EmployeesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chooseLocationButton.setOnClickListener {
            findNavController().navigate(R.id.action_addEmployeeFragment_to_chooseLocationForEmployee)

        }
        binding.cancelButtonLocations.setOnClickListener {
            findNavController().popBackStack()
        }


        val currentUser = FirebaseAuth.getInstance().currentUser


        binding.addEmployeeButton.setOnClickListener {

            var name = binding.newEmployeeNameEditText.text.toString()
            var surname = binding.newEmployeeSurnameEditText.text.toString()
            var email = binding.newEmployeeEmailEditText.text.toString()
            var phone = binding.newEmployeePhoneEditText.text.toString()
            var hireDate = binding.newEmployeeHireDateEditText.text.toString()
            var birthDate = binding.newEmployeeBirthdateEditText.text.toString()
            var employeeId = binding.newEmployeIdEditText.text.toString()
            val uniqueId = UUID.randomUUID().toString()


            var locationsList = emptyList<Location>()
            var positionsList = emptyList<Position>()

            if (
                surname.isNotEmpty() &&
                employeeId.isNotEmpty() &&
                email.isNotEmpty() &&
                phone.isNotEmpty() &&
                hireDate.isNotEmpty() &&
                birthDate.isNotEmpty()
            ) {
                var newEmployee = Employee(
                    uniqueId,
                    name,
                    surname,
                    "",
                    email,
                    phone,
                    "",
                    employeeId,
                    hireDate,
                    birthDate,
                    locationsList,
                    positionsList
                )

                if (currentUser?.uid != null) {
                    viewModel.addEmployeeToEmployer(currentUser.uid, newEmployee)
                    findNavController().popBackStack()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all sections.", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }
}

