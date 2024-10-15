package com.example.onthetime.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class  Employee(
    val id:String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val prefferedFirstsName: String = "",
    val profilePhotoPath:String = "",
    val status: String = "",
    val email: String? = "",
    val phoneNumber: String? = "",
    val password: String? = "",
    val employerId:String? = "",
    val birthDate:String? = "" ,
    val hireDate:String? = "",
    val locations:List<Location>?    = emptyList(),
    val positions:List<Position>? = emptyList(),
    val shiftList:List<Shift>? = emptyList()
):Parcelable
