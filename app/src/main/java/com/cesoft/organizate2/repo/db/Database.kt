package com.cesoft.organizate2.repo.db

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database


/**
 * Created by ccasanova on 23/05/2018
 */
//https://developer.android.com/guide/topics/data/autobackup
@Database(entities = [(TaskTable::class)], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun dao(): TaskDao
}