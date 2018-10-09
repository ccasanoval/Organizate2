package com.cesoft.organizate2.repo.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.cesoft.organizate2.entity.AlertDateEntity


@Entity(tableName = TablesContracts.TABLE_NAME_ALERT_DATE)
@TypeConverters(Converters::class)
class AlertDateTable(
        @PrimaryKey
        val id: Int,
        val minutes: IntArray,
        val hours: IntArray,
        val daysOfWeek: IntArray,
        val daysOfMonth: IntArray,
        val months: IntArray
        //val years: IntArray,
    )
{
    fun toEntity() = AlertDateEntity(id, minutes, hours, daysOfWeek, daysOfMonth, months)
}