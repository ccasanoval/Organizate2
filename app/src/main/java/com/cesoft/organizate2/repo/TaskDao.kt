package com.cesoft.organizate2.repo

import android.arch.persistence.room.*
import android.arch.persistence.room.Dao

/**
 * Created by ccasanova on 23/05/2018
 */
@Dao
interface TaskDao {
    @Insert
    fun insert(task: TaskTable)
    @Insert
    fun insert(task: List<TaskTable>)
    @Query("SELECT * FROM TaskTable WHERE id = :id")
    fun selectById(id: Int) : TaskTable
    @Query("SELECT * FROM TaskTable ")
    fun select() : List<TaskTable>
    @Update
    fun update(task: TaskTable)
    @Delete
    fun delete(task: TaskTable)
}
