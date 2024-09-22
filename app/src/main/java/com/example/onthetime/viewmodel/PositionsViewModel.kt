package com.example.onthetime.viewmodel

import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthetime.R
import com.example.onthetime.model.Position
import com.example.onthetime.repository.EmployerRepository
import com.google.firebase.auth.FirebaseAuth

class PositionsViewModel() : ViewModel() {

    private val _positions = MutableLiveData<List<Position>>()
    val positions: LiveData<List<Position>> get() = _positions

    private val _count_of_positions = MutableLiveData<Int>()
    val count_of_positions: LiveData<Int> get() = _count_of_positions

    private val _addPositionResult = MutableLiveData<Boolean>()
    val addPositionResult: LiveData<Boolean> get() = _addPositionResult

    private val repository = EmployerRepository()
    val employerId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        fetchPositions(employerId)
        _count_of_positions.value = _positions.value?.size
    }


    fun fetchPositions(employerId: String) {
        repository.getPositionsWithSnapshot(employerId) { positionsList ->
            _positions.value = positionsList
        }
    }


    fun addPositionToEmployer(employerId: String, position: Position) {
        repository.addPositionToEmployer(employerId, position) { success ->
            _addPositionResult.value = success
        }
    }

}