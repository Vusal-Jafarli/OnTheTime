package com.example.onthetime.model


import com.google.firebase.Timestamp

data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Timestamp? = null
)
