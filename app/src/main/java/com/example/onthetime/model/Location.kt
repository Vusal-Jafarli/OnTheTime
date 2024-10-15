package com.example.onthetime.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val name: String = "",
    val address: String = ""
):Parcelable