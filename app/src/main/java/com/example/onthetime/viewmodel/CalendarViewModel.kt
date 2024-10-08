package com.example.onthetime.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthetime.model.Date
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel : ViewModel() {

    private val _mainList = MutableLiveData<List<Date>>().apply { value = emptyList() }
    val mainList: MutableLiveData<List<Date>> get() = _mainList


    private val _daysOfWeek = MutableLiveData<List<Pair<String, String>>>()
    val daysOfWeek: MutableLiveData<List<Pair<String, String>>> get() = _daysOfWeek


    private val _days = MutableLiveData<List<Int>>().apply { value = emptyList() }
    val days: MutableLiveData<List<Int>> get() = _days

    private val _today = MutableLiveData<Int>()
    val today: MutableLiveData<Int> get() = _today

    private val _toMonth = MutableLiveData<Int>()
    val toMonth: MutableLiveData<Int> get() = _toMonth

    private val _pointMonth = MutableLiveData<Int>()
    val pointMonth: MutableLiveData<Int> get() = _pointMonth


    private val _weekDayInit = MutableLiveData<String>()
    val weekDayInit: MutableLiveData<String> get() = _weekDayInit

    init {
        _today.value = LocalDate.now().dayOfMonth
        _toMonth.value = LocalDate.now().monthValue
        _pointMonth.value = LocalDate.now().monthValue

        weekDayInit.value =
            LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

        weekDayInit.value  = weekDayInit.value.toString().substring(0,3)

    }


    fun sendDateToLoadDays() {
        val day = _mainList.value?.first()!!.day
        val month = _mainList.value?.first()!!.month
        val year = _mainList.value?.first()!!.year
        loadDays(day, month, year)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDays(day: Int, month: Int, year: Int): List<Date> {

        var list: MutableList<Date> = mutableListOf()

        val firstDayOfLoop = LocalDate.of(year, month, day)
        val lengthOfMonth = firstDayOfLoop.lengthOfMonth()




        if (lengthOfMonth - day >= 7) {
            for (dayLoop in day..day + 6) {
                val date = LocalDate.of(year, month, dayLoop)
                val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

                var exampleDate = Date(year, month, dayLoop, dayOfWeek, "00:00", "23:59")
                list.add(exampleDate)
            }
        } else { //Eger ayin axrina 7 gunden az muddet qalibsa
            var neededDays = 7 - (lengthOfMonth - day)

            for (dayLoop in day..lengthOfMonth) {
                val date = LocalDate.of(year, month, dayLoop)
                val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                var exampleDate = Date(year, month, dayLoop, dayOfWeek, "00:00", "23:59")
                list.add(exampleDate)
            }

            var newMonth = month
            var newYear = year
            var newDay = day

            if (month == 12) //Month == December
            {
                newMonth = 1
                newYear = year + 1
                newDay = 1
            } else {
                newMonth = month + 1
                newYear = year
                newDay = 1
            }

            for (dayLoop in 1..neededDays - 1) {
                val date = LocalDate.of(newYear, newMonth, dayLoop)
                val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                var exampleDate = Date(newYear, newMonth, dayLoop, dayOfWeek, "00:00", "23:59")
                list.add(exampleDate)
            }
        }

        var listDays: MutableList<Int> = mutableListOf()
        var listDaysOfWeek: MutableList<Pair<String, String>> = mutableListOf()


        for (i in 0..list.size - 1) {
            listDays.add(list[i].day)
            listDaysOfWeek.add(list[i].day.toString() to list[i].dayOfWeek)
        }

        mainList.value = list
        _mainList.value = list
        _days.value = listDays
        _daysOfWeek.value = listDaysOfWeek
        daysOfWeek.value = listDaysOfWeek
        _pointMonth.value = month
        pointMonth.value = month

        return list
    }

}
