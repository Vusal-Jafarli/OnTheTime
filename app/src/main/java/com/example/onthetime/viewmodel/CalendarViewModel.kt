package com.example.onthetime.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthetime.model.Date
import com.example.onthetime.model.Shift
import com.example.onthetime.repository.EmployeeRepository
import com.example.onthetime.repository.EmployerRepository
import com.example.onthetime.ui.fragments.ShiftsFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel() : ViewModel() {


    private var employeeRepository = EmployeeRepository()
    private var employerRepository = EmployerRepository()
    private var status = ""

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

    private val _thisYear = MutableLiveData<Int>()
    val thisYear: MutableLiveData<Int> get() = _thisYear

    private val _pointMonth = MutableLiveData<Int>()
    val pointMonth: MutableLiveData<Int> get() = _pointMonth

    private val _daysAndShifts =
        MutableLiveData<MutableList<Pair<Pair<String, String>, List<Shift>>>>()
    val daysAndShifts: MutableLiveData<MutableList<Pair<Pair<String, String>, List<Shift>>>> get() = _daysAndShifts

    private val _daysAndShiftsForEmployee =
        MutableLiveData<MutableList<Pair<Pair<String, String>, List<Shift>>>>()
    val daysAndShiftsForEmployee: MutableLiveData<MutableList<Pair<Pair<String, String>, List<Shift>>>> get() = _daysAndShiftsForEmployee


    private val _weekDayInit = MutableLiveData<String>()
    val weekDayInit: MutableLiveData<String> get() = _weekDayInit

    init {
        _today.value = LocalDate.now().dayOfMonth
        _toMonth.value = LocalDate.now().monthValue
        _thisYear.value = LocalDate.now().year
        _pointMonth.value = LocalDate.now().monthValue

        weekDayInit.value =
            LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

        weekDayInit.value = weekDayInit.value.toString().substring(0, 3)

        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        employerRepository.getUserStatus(userID){
            status ->
            if(status == "employee")
                this.status = status

            else if(status == "employer")
                this.status = status
        }

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


        var newMonth = month
        var newYear = year
        var newDay = day

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

            newMonth = month
            newYear = year
            newDay = day

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

        _mainList.value = list
        _days.value = listDays
        _daysOfWeek.value = listDaysOfWeek
        _pointMonth.value = newMonth

        val userID = FirebaseAuth.getInstance().currentUser!!.uid


        if (status == "employee") {

            employeeRepository.getShiftsByEmployee(userID) { listForMySchedule ->
                daysAndShiftsForEmployee.value = sortList(listForMySchedule)
            }
            employeeRepository.getEmployeeEmployerID(userID) { employerID ->
                employeeRepository.getAllShiftsByEmployerID(employerID) { list ->
                    daysAndShifts.value = sortList(list)
                }
            }
        }
        else if (status == "employer") {

            employerRepository.getEmployerID(userID) { id ->
                if (id != null) {
                    employeeRepository.getAllShiftsByEmployerID(id) { list ->
                        daysAndShifts.value = sortList(list)
                    }
                } else
                    Log.d("DaysShifts", "Employer id null -dur.")
            }

        }





        return list
    }


//    fun shiftList():List<Shift>{
//
//    }

    fun sortList(
        shiftList: MutableList<Shift>
    ): MutableList<Pair<Pair<String, String>, List<Shift>>> {

        var lastList: MutableList<Pair<Pair<String, String>, List<Shift>>> = mutableListOf()
        var index = 0

        for (day in mainList.value!!) {
            var listForIteration: MutableList<Shift> = mutableListOf()

            for (everyShift in shiftList) {

                if (everyShift.valuedDatesList != null) {

                    for (valuedDate in everyShift.valuedDatesList) {

                        //Bu hisse bele sert ile yazilmamamlidir dsaha optimizsaiya olunmalidir.
                        if (valuedDate.day == mainList.value!![index].day && valuedDate.month == mainList.value!![index].month && valuedDate.year == mainList.value!![index].year) {
                            listForIteration.add(everyShift)
                            break
                        }


                    }
                }
            }
//            var regularList: List<Shift> = listForIteration
            var daysOfWeekItem = Pair(day.day.toString(), day.dayOfWeek.substring(0, 3))
            var newPair = Pair(daysOfWeekItem, listForIteration)

            lastList.add(newPair)
            index += 1

        }

        return lastList
    }
}
