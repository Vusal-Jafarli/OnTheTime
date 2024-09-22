package com.example.onthetime.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthetime.model.Location
import com.example.onthetime.model.Position
import com.example.onthetime.repository.EmployerRepository
import com.google.firebase.auth.FirebaseAuth

class LocationsViewModel : ViewModel() {

    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>> get() = _locations

    private val _count_of_locations = MutableLiveData<Int>()
    val count_of_locations: LiveData<Int> get() = _count_of_locations

    private val _addLocationResult = MutableLiveData<Boolean>()
    val addLocationResult: LiveData<Boolean> get() = _addLocationResult

    private val repository = EmployerRepository()
    val employerId = FirebaseAuth.getInstance().currentUser!!.uid


    init {
        fetchLocations(employerId)
        _count_of_locations.value = _locations.value?.size
    }


    fun fetchLocations(employerId: String) {
        repository.getLocationsWithSnapshot(employerId) { locationList ->
            _locations.value = locationList
        }
    }


    fun addLocationToEmployer(employerId: String, location: Location) {
        repository.addLocationToEmployer(employerId, location) { success ->
            _addLocationResult.value = success
        }
    }
}