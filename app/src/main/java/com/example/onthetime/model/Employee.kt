package com.example.onthetime.model

data class  Employee(
    val id:String = "",
    val firstName: String = "",
    val lastName: String = "",
    val prefferedFirstsName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val employerId:String = "",
    val birthDate:String = "" ,
    val hireDate:String = "",
    val locations:List<Location> = emptyList(),
    val positions:List<Position> = emptyList(),
)
