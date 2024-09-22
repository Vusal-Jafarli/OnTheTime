package com.example.onthetime.model

enum class Month(val monthNumber: Int) {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    companion object {
        fun fromNumber(number: Int): Month? {
            return values().find { it.monthNumber == number }
        }
    }
}
