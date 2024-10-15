package com.example.onthetime.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Position (
    val name:String = "",
    val colorCode:Int= 0,
):Parcelable