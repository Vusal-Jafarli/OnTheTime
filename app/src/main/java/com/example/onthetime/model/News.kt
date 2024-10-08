package com.example.onthetime.model

import android.icu.util.Calendar

data class News(
    val owner: String? = null,
    val authornameSurname: String = "",
    val message: String = "",
    val description: String = "",
    val time: Time? = null,
    val date: Date? = null,
    val like_count: Int = 0,
    val view_count: Int = 0,
    val comment_count: Int = 0,
    val comments: List<Comment> = emptyList(),
)