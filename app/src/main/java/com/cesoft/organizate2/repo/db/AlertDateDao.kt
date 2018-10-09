package com.cesoft.organizate2.repo.db

import android.arch.persistence.room.*

/**
 * Created by ccasanova on 08/10/2018
 */
@Dao
interface AlertDateDao {
    @Insert
    fun insert(task: AlertDateTable)

    @Insert
    fun insert(task: List<AlertDateTable>)

    @Update
    fun update(task: AlertDateTable)

    @Delete
    fun delete(task: AlertDateTable)

    @Query("DELETE FROM ${TablesContracts.TABLE_NAME_ALERT_DATE}")
    fun nukeTable()

    @Suppress("AndroidUnresolvedRoomSqlReference")
    @Query("SELECT * FROM ${TablesContracts.TABLE_NAME_ALERT_DATE} WHERE id = :id")
    fun selectById(id: Int) : AlertDateTable?
}
