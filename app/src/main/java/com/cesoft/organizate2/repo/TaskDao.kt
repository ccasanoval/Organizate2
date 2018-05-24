package com.cesoft.organizate2.repo

import android.arch.lifecycle.LiveData
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
    @Query("SELECT id, idSuper, name, ordering FROM TaskTable ")
    fun selectRedux() : List<TaskReduxTable>
    @Query("SELECT * FROM TaskTable ")
    fun selectAsync() : LiveData<List<TaskTable>>

    @Update
    fun update(task: TaskTable)
    @Delete
    fun delete(task: TaskTable)

    //https://medium.com/google-developers/room-rxjava-acb0cd4f3757
    //https://proandroiddev.com/android-room-handling-relations-using-livedata-2d892e40bd53
}
