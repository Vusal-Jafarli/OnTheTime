package com.example.onthetime.repository

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.onthetime.R
import com.example.onthetime.model.Employee
import com.example.onthetime.model.Employer
import com.example.onthetime.model.Location
import com.example.onthetime.model.News
import com.example.onthetime.model.Position
import com.example.onthetime.model.Shift
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firestore.v1.ArrayValue
import com.squareup.picasso.Picasso

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

    fun addEmployeeToEmployeesList(
        employerId: String,
        employee: Employee,
        onComplete: (Boolean) -> Unit
    ) {
        val employerRef = firestore.collection("employees").document(employerId)

        employerRef.set(
            mapOf("employees" to FieldValue.arrayUnion(employee))
        ).addOnSuccessListener {
            onComplete(true)
        }
            .addOnFailureListener {
                onComplete(false)
            }
    }
    fun addShiftToEmployee2(
        employerId: String,
        employeeId: String,
        newShift: Shift,
        onComplete: (Boolean) -> Unit
    ) {
        val employeeRef = firestore.collection("employers").document(employerId)
            .collection("employees").document(employeeId)

        employeeRef.update("shiftList", FieldValue.arrayUnion(newShift))
            .addOnSuccessListener {
                Log.d("Firestore", "Shift successfully added to employee's shift list.")
                onComplete(true) // Başarı durumu
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error adding shift to employee's shift list", e)
                onComplete(false)
            }
    }

    fun addShiftToEmployee(
        employerId: String,
        employeeId: String,
        shift: Shift,
        onComplete: (Boolean) -> Unit
    ) {
        val employerRef = firestore.collection("employees").document(employeeId)


        employerRef.collection("employees").document(employeeId)
            .update("shiftList", FieldValue.arrayUnion(shift))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error updating shift list", e)

                onComplete(false)
            }
    }

    fun updateProfilePhotoPath(
        employerId: String,
        newPhotoPath: String,
        onComplete: (Boolean) -> Unit
    ) {
        val employerRef = firestore.collection("employers").document(employerId)

        employerRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val oldPhotoPath = document.getString("profilePhotoPath")

                    if (oldPhotoPath != null && oldPhotoPath != "") {
                        oldPhotoPath.let {
                            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(it)
                            storageRef.delete()
                                .addOnSuccessListener {
                                    println("Old photo deleted from storage.")
                                }
                                .addOnFailureListener {
                                    println("Error deleting old photo: ${it.message}")
                                }
                        }
                    }

                    employerRef.update("profilePhotoPath", newPhotoPath)
                        .addOnSuccessListener {
                            onComplete(true)
                        }
                        .addOnFailureListener {
                            onComplete(false)
                        }
                } else {
                    onComplete(false)
                }
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }


    fun getEmployerPhotoPath(employerId: String, onComplete: (String?) -> Unit) {
        val employerRef =
            FirebaseFirestore.getInstance().collection("employers").document(employerId)

        employerRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profilePhotoPath = document.getString("profilePhotoPath")
                    onComplete(profilePhotoPath)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun getEmployerName(employerId: String, onComplete: (String?) -> Unit) {
        val employerRef =
            FirebaseFirestore.getInstance().collection("employers").document(employerId)

        employerRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profilePhotoPath = document.getString("firstName")
                    onComplete(profilePhotoPath)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun getEmployerLastName(employerId: String, onComplete: (String?) -> Unit) {
        val employerRef =
            FirebaseFirestore.getInstance().collection("employers").document(employerId)

        employerRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profilePhotoPath = document.getString("lastName")
                    onComplete(profilePhotoPath)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun getEmployerPassword(employerId: String, onComplete: (String?) -> Unit) {
        val employerRef =
            FirebaseFirestore.getInstance().collection("employers").document(employerId)

        employerRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val password = document.getString("password")
                    onComplete(password)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun getEmployerEmail(employerId: String, onComplete: (String?) -> Unit) {
        val employerRef =
            FirebaseFirestore.getInstance().collection("employers").document(employerId)

        employerRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profilePhotoPath = document.getString("email")
                    onComplete(profilePhotoPath)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun getEmployerID(employerId: String, onComplete: (String?) -> Unit) {
        val employerRef =
            FirebaseFirestore.getInstance().collection("employers").document(employerId)

        employerRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profilePhotoPath = document.getString("id")
                    onComplete(profilePhotoPath)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun getEmployerPhoneNumber(employerId: String, onComplete: (String?) -> Unit) {
        val employerRef =
            FirebaseFirestore.getInstance().collection("employers").document(employerId)

        employerRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profilePhotoPath = document.getString("phoneNumber")
                    onComplete(profilePhotoPath)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }


    fun getEmployerDateBirth(employerId: String, onComplete: (String?) -> Unit) {
        val employerRef =
            FirebaseFirestore.getInstance().collection("employers").document(employerId)

        employerRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profilePhotoPath = document.getString("birthDate")
                    onComplete(profilePhotoPath)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }


    fun addProfilePhotoPath(
        employerId: String,
        photoPath: String,
        onComplete: (Boolean) -> Unit
    ) {
        val employerRef = firestore.collection("employers").document(employerId)

        employerRef.update("profilePhotoPath", FieldValue.arrayUnion(photoPath))
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
