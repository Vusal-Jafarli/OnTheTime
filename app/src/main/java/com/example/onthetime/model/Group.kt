package com.example.onthetime.model

data class Group(
    val name: String = "",
    val description:String = "",
    val startingDate:String = "",
    val participants: List<Employer> = emptyList()
)