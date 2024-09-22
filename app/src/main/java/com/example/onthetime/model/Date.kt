package com.example.onthetime.model

data class Date(
    var year: Int = 2024,
    var month: Int = 9,
    var day: Int = 22,
    var dayOfWeek: String = "",
    var startTime:String = "",
    var endTime:String = ""
    )

//LocalDate.of(year,month,day).dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())