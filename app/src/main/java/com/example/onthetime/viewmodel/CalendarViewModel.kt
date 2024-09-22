package com.example.onthetime.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class CalendarViewModel : ViewModel() {

    private val _month = MutableLiveData<String>().apply { value = "Decem" }
    val month: MutableLiveData<String> get() = _month

    private val _currentMonth = MutableLiveData<Int>().apply { value = 9 }
    val currentMonth: MutableLiveData<Int> get() = _currentMonth

    private val _currentYear = MutableLiveData<Int>().apply { value = 2024 }
    val currentYear: MutableLiveData<Int> get() = _currentYear


    private val _index = MutableLiveData<Int>().apply { value = 0 }
    val index: MutableLiveData<Int> get() = _index

    private val _daysOfWeek = MutableLiveData<List<List<Pair<String, String>>>>()
    val daysOfWeek: MutableLiveData<List<List<Pair<String, String>>>> get() = _daysOfWeek

    private val _currentDateDay = MutableLiveData<String>().apply { value = "21" }
    public val currentDateDay: MutableLiveData<String> get() = _currentDateDay


    private val _startDate = MutableLiveData<String>().apply { value = "21" }
    public val startDate: MutableLiveData<String> get() = _startDate

    private val _startMonth = MutableLiveData<Int>().apply { value = 9 }
    public val startMonth: MutableLiveData<Int> get() = _startMonth




    init {

        currentDateDay.value = "21"
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDaysForMonth(year: Int?, month: Int?) {
        var year1 = 2024
        var month1 = 9

        if (year == null || month == null) {
            year1 = 2024
            month1 = 9
        } else {
            year1 = year
            month1 = month
        }

        _daysOfWeek.value = getDaysOfWeekForMonth(year1, month1)
        _month.value = Month.of(month1).getDisplayName(TextStyle.FULL, Locale.getDefault())

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun nextPage() {
        if (_index.value!! < (_daysOfWeek.value?.size!! - 1)) {
            _index.value = _index.value!! + 1
        }
    }

    fun previousPage() {
        if (_index.value!! > 0) {
            _index.value = _index.value!! - 1
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDaysOfWeekForMonth(year: Int?, month: Int?): List<List<Pair<String, String>>> {

        var year1 = 2024
        var month1 = 9

        if (year == null || month == null) {
            year1 = 2024
            month1 = 9
        } else {
            year1 = year
            month1 = month
        }

        val daysOfWeekList = mutableListOf<Pair<String, String>>()
        val firstDayOfMonth = LocalDate.of(year1, month1, currentDateDay.value!!.toInt())
        val lengthOfMonth = firstDayOfMonth.lengthOfMonth()

        val currentDate = _currentDateDay.value!!.toInt()

        for (day in currentDate..lengthOfMonth) {
            val date = LocalDate.of(year1, month1, day)
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            daysOfWeekList.add(day.toString() to dayOfWeek)
        }
        var daysNeeded = 0

        val lastWeekDays = daysOfWeekList.size % 7
        if (lastWeekDays != 0) {
            val nextMonth = if (month1 == 12) 1 else month1 + 1
            val nextYear = if (month1 == 12) year1 + 1 else year1

            daysNeeded = 7 - lastWeekDays
            for (day in 1..daysNeeded) {
                try {
                    val date = LocalDate.of(nextYear, nextMonth, day)
                    val dayOfWeek =
                        date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    daysOfWeekList.add(day.toString() to dayOfWeek)
                } catch (e: Exception) {
                    //Xeta bas vererse
                    break
                }
            }
        } else {
            val nextMonth = if (month1 == 12) 1 else month1 + 1
            val nextYear = if (month1 == 12) year1 + 1 else year1
        }


        val groupedDays = mutableListOf<List<Pair<String, String>>>()
        for (i in daysOfWeekList.indices step 7) {
            val end = minOf(i + 7, daysOfWeekList.size)
            groupedDays.add(daysOfWeekList.subList(i, end))
        }

        _currentDateDay.value = daysOfWeekList.last().first

        return groupedDays
    }

}
