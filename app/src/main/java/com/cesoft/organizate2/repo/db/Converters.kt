package com.cesoft.organizate2.repo.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class Converters {
    @TypeConverter
    fun string2ArrayList(value: String): IntArray {
        val listType = object : TypeToken<IntArray>(){}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun ArrayList2String(list: IntArray): String = Gson().toJson(list)

    @TypeConverter
    fun Timestamp2Date(value: Long?): Date? = if(value == null) null else Date(value)
    @TypeConverter
    fun date2Timestamp(date: Date?): Long? = date?.time!!.toLong()
}