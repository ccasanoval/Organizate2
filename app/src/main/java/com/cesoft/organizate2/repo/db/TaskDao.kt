package com.cesoft.organizate2.repo.db

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

    @Update
    fun update(task: TaskTable)

    @Delete
    fun delete(task: TaskTable)
    @Query("DELETE FROM ${TaskTableContract.TABLE_NAME}")
    fun nukeTable()

    @Query("SELECT * FROM ${TaskTableContract.TABLE_NAME} WHERE id = :id")
    fun selectById(id: Int) : TaskTable?
    @Query("SELECT id, idSuper, name, level FROM ${TaskTableContract.TABLE_NAME}")
    fun selectRedux() : List<TaskReduxTable>
    @Query("SELECT * FROM ${TaskTableContract.TABLE_NAME}")
    fun select() : List<TaskTable>

    @Query("SELECT id, idSuper, name, level FROM ${TaskTableContract.TABLE_NAME}")
    fun selectReduxLive() : LiveData<List<TaskReduxTable>>
    @Query("SELECT * FROM ${TaskTableContract.TABLE_NAME} WHERE id = :id")
    fun selectByIdLive(id: Int) : LiveData<TaskTable>

    //@Query("SELECT * FROM TaskTable")
    //fun selectAsync() : LiveData<List<TaskTable>>

    //https://medium.com/google-developers/room-rxjava-acb0cd4f3757
    //https://proandroiddev.com/android-room-handling-relations-using-livedata-2d892e40bd53
    //https://proandroiddev.com/testing-the-un-testable-and-beyond-with-android-architecture-components-part-1-testing-room-4d97dec0f451
}
