package com.cesoft.organizate2.entity

import java.util.*


data class AlertDateEntity(
        val id: Int,
        val minutes: IntArray,
        val hours: IntArray,
        val daysOfWeek: IntArray,
        val daysOfMonth: IntArray,
        val months: IntArray)
{
    companion object
    {
        const val TYPE_MINUTES = 0
        const val TYPE_HOURS = 1
        const val TYPE_DAYS_WEEK = 2
        const val TYPE_DAYS_MONTH = 3
        const val TYPE_MONTH = 4

        const val MONDAY = 0
        const val TUESDAY = 1
        const val WEDNESDAY = 2
        const val THURSDAY = 3
        const val FRIDAY = 4
        const val SATURDAY = 5
        const val SUNDAY = 6

        const val JANUARY = 0
        const val FEBRUARY = 1
        const val MARCH = 2
        const val APRIL = 3
        const val MAY = 4
        const val JUNE = 5
        const val JULY = 6
        const val AUGUST = 7
        const val SEPTEMBER = 8
        const val OCTOBER = 9
        const val NOVEMBER = 10
        const val DECEMBER = 11
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AlertDateEntity
        if(id != other.id) return false
        if(!Arrays.equals(minutes, other.minutes)) return false
        if(!Arrays.equals(hours, other.hours)) return false
        if(!Arrays.equals(daysOfWeek, other.daysOfWeek)) return false
        if(!Arrays.equals(daysOfMonth, other.daysOfMonth)) return false
        if(!Arrays.equals(months, other.months)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + Arrays.hashCode(minutes)
        result = 31 * result + Arrays.hashCode(hours)
        result = 31 * result + Arrays.hashCode(daysOfWeek)
        result = 31 * result + Arrays.hashCode(daysOfMonth)
        result = 31 * result + Arrays.hashCode(months)
        return result
    }
}