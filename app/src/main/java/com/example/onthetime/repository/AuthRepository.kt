package com.example.onthetime.repository

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.onthetime.model.Employer
//import com.example.onthetime.model.User
//import com.example.onthetime.model.UserDatabase
//import com.example.onthetime.model.toUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var sharedPreferences: SharedPreferences



    fun signUp(
        context: Context,
        employer: Employer,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(employer.email, employer.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firestore.collection("employers").document(auth.currentUser!!.uid)
                        .set(employer)
                        .addOnSuccessListener {

                        }
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
                    var currentUser = FirebaseAuth.getInstance().currentUser

                    onSucces()
                } else {
                    onFailure(task.exception?.message.toString())
                }
            }

    }

}




