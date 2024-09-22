package com.example.onthetime.repository

import android.util.Log
import com.example.onthetime.model.Employee
import com.example.onthetime.model.Employer
import com.example.onthetime.model.Location
import com.example.onthetime.model.News
import com.example.onthetime.model.Position
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class EmployerRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun addPositionToEmployer(
        employerId: String,
        position: Position,
        onComplete: (Boolean) -> Unit
    ) {
        val employerRef = firestore.collection("employers").document(employerId)

        employerRef.update("positions", FieldValue.arrayUnion(position))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }


    fun addLocationToEmployer(
        employerId: String,
        location: Location,
        onComplete: (Boolean) -> Unit
    ) {
        val employerRef = firestore.collection("employers").document(employerId)

        employerRef.update("locations", FieldValue.arrayUnion(location))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun addNewsToEmployer(
        employerId: String,
        news: News,
        onComplete: (Boolean) -> Unit
    ) {
        val employerRef = firestore.collection("employers").document(employerId)

        employerRef.update("news", FieldValue.arrayUnion(news))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun addEmployeeToEmployer(
        employerId: String,
        employee: Employee,
        onComplete: (Boolean) -> Unit
    ) {
        val employerRef = firestore.collection("employers").document(employerId)

        employerRef.update("employees", FieldValue.arrayUnion(employee))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }


    fun getPositionsWithSnapshot(employerId: String, callback: (List<Position>) -> Unit) {
        val employerRef = firestore.collection("employers").document(employerId)
        employerRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                Log.e("FirestoreError", "Error fetching positions: ${exception.message}")
                callback(emptyList())
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val positionsList =
                    documentSnapshot.toObject(Employer::class.java)?.positions ?: emptyList()
                callback(positionsList)
            } else {
                callback(emptyList())
            }
        }
    }

    fun getLocationsWithSnapshot(employerId: String, callback: (List<Location>) -> Unit) {
        val employerRef = firestore.collection("employers").document(employerId)
        employerRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                Log.e("FirestoreError", "Error fetching positions: ${exception.message}")
                callback(emptyList())
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val locationList =
                    documentSnapshot.toObject(Employer::class.java)?.locations ?: emptyList()
                callback(locationList)
            } else {
                callback(emptyList())
            }
        }
    }


    fun getNewsWithSnapshot(employerId: String, callback: (List<News>) -> Unit) {
        val employerRef = firestore.collection("employers").document(employerId)
        employerRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                Log.e("FirestoreError", "Error fetching positions: ${exception.message}")
                callback(emptyList())
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val newsList =
                    documentSnapshot.toObject(Employer::class.java)?.news ?: emptyList()
                callback(newsList)
            } else {
                callback(emptyList())
            }
        }
    }

    fun getEmployeesWithSnapshot(employerId: String, callback: (List<Employee>) -> Unit) {
        val employerRef = firestore.collection("employers").document(employerId)
        employerRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                Log.e("FirestoreError", "Error fetching positions: ${exception.message}")
                callback(emptyList())
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val employeeList =
                    documentSnapshot.toObject(Employer::class.java)?.employees ?: emptyList()
                callback(employeeList)
            } else {
                callback(emptyList())
            }
        }
    }




    fun getPositions(employerId: String, callback: (List<Position>) -> Unit) {
        val employerRef = firestore.collection("employers").document(employerId)
        employerRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val positionsList =
                        document.toObject(Employer::class.java)?.positions ?: emptyList()
                    callback(positionsList)
                } else {
                    callback(emptyList())
                }

            }
            .addOnFailureListener { exception ->
                callback(emptyList())
                Log.e("FirestoreError", "Error fetching positions: ${exception.message}")

            }
    }

}
