package com.cesoft.organizate2.repo

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database


/**
 * Created by ccasanova on 23/05/2018
 */
@Database(entities = arrayOf(TaskTable::class), version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun dao(): TaskDao
}