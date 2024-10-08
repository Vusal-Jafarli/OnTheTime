package com.example.onthetime.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthetime.model.Employee
import com.example.onthetime.model.Location
import com.example.onthetime.repository.EmployerRepository
import com.google.firebase.auth.FirebaseAuth

class EmployeesViewModel : ViewModel() {

    private val _employees = MutableLiveData<List<Employee>>()
    val employees: LiveData<List<Employee>> get() = _employees

    private val _employerId = MutableLiveData<String?>()
    val id: MutableLiveData<String?> get() = _employerId

    private val _countOfEmployees = MutableLiveData<Int>()
    val countOfEmployees: LiveData<Int> get() = _countOfEmployees

    private val _addEmployeeResult = MutableLiveData<Boolean>()
    val addEmployeeResult: LiveData<Boolean> get() = _addEmployeeResult

    private val repository = EmployerRepository()
    val employerId = FirebaseAuth.getInstance().currentUser!!.uid


    init {
        fetchEmployees(employerId)
        _countOfEmployees.value = _employees.value?.size
    }


    fun fetchEmployees(employerId: String) {
        repository.getEmployeesWithSnapshot(employerId) { employeeList ->
            _employees.value = employeeList
        }
    }


    fun addEmployeeToEmployer(employerId: String, employee: Employee) {
        repository.addEmployeeToEmployer(employerId, employee) { success ->
            _addEmployeeResult.value = success
        }
    }

    fun addEmployeeToEmployees(employerId: String, employee: Employee) {
        repository.addEmployeeToEmployeesList(employerId,employee){ success ->
            _addEmployeeResult.value = success
        }
    }
    fun getEmployerId(employerId: String){
        repository.getEmployerID(employerId){ id ->
            _employerId.value = id
        }
    }
}