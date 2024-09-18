package com.example.onthetime.model


data class Employer(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val employees:List<Employee> = emptyList(),
    val positions:List<Position> = emptyList(),
    val locations:List<Location> = emptyList(),
    val groups:List<Group> = emptyList(),
    val news:List<News> = emptyList(),
)
