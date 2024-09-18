package com.example.onthetime.repository

import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.onthetime.model.Employer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()


    fun signUp(
        employer: Employer,
//        user2: HashMap<String, String>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(employer.email, employer.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firestore.collection("employers").document(auth.currentUser!!.uid)
                        .set(employer)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure(e.message.toString()) }


                } else {
                    onFailure(task.exception?.message.toString())
                }
            }
    }

    fun login(
        email: String,
        password: String,
        onSucces: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSucces()
                } else {
                    onFailure(task.exception?.message.toString())
                }
            }

    }

}




