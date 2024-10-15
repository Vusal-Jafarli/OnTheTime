package com.example.onthetime.model

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
@Parcelize
data class Date(
    var year: Int = 2024,
    var month: Int = 9,
    var day: Int = 22,
    var dayOfWeek: String = "",
    var startTime: String = "",
    var endTime: String = ""
):Parcelable


@RequiresApi(Build.VERSION_CODES.O)
fun Date.isBefore(other: Date?): Boolean? {
    if (other != null) {
        val thisDate = LocalDate.of(this.year, this.month, this.day)
        val otherDate = LocalDate.of(other.year, other.month, other.day)
        return thisDate.isBefore(otherDate)
    }

    return null
}

@RequiresApi(Build.VERSION_CODES.O)
fun Date.isAfter(other: Date?): Boolean? {
    if (other != null) {
        val thisDate = LocalDate.of(this.year, this.month, this.day)
        val otherDate = LocalDate.of(other.year, other.month, other.day)
        return thisDate.isAfter(otherDate)
    }

    return null
}

@RequiresApi(Build.VERSION_CODES.O)
fun Date.addDays(daysToAdd: Long): Date {
    val localDate = LocalDate.of(this.year, this.month, this.day).plusDays(daysToAdd)
    val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    return this.copy(year = localDate.year, month = localDate.monthValue, day = localDate.dayOfMonth,dayOfWeek)
}