package com.example.onthetime.model


data class Employer(
    val id:String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthDate: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val profilePhotoPath:String = "",
    val employees:List<Employee> = emptyList(),
    val positions:List<Position> = emptyList(),
    val locations:List<Location> = emptyList(),
    val groups:List<Group> = emptyList(),
    val news:List<News> = emptyList(),
)
//fun Employer.toUser(): User {
//    return User(
//        firstName = this.firstName,
//        lastName = this.lastName,
//        password = this.password,
//        email = this.email
//    )
//}