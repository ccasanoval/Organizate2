package com.cesoft.organizate2.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.cesoft.organizate2.entity.Task
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.util.exception.Failure
import com.cesoft.organizate2.util.functional.Either
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.repo.db.TaskReduxTable
import com.cesoft.organizate2.repo.db.TaskTable
import com.cesoft.organizate2.util.Log
import javax.inject.Inject

/**
 * Created by ccasanova on 24/05/2018
 */
interface TaskRepo {
    fun getTasksList(): Either<Failure, LiveData<List<TaskReduxEntity>>>
    fun getTaskDetails(id: Int): Either<Failure, LiveData<TaskEntity>>
    fun saveTasksDetails(task: TaskEntity): Either<Failure, Unit>

    ///---------------------------------------------------------------------------------------------
    //class Network

    ///---------------------------------------------------------------------------------------------
    class DataBase
    @Inject constructor(private val db: Database)
        : TaskRepo {

        override fun getTasksList(): Either<Failure, LiveData<List<TaskReduxEntity>>> {
            return try {
                val tasks0 : LiveData<List<TaskReduxTable>> = db.dao().selectReduxLive()
                //val tasks = tasks0.value!!.map { it.toTaskEntity() }
                val tasks1 : LiveData<List<TaskReduxEntity>> = Transformations.map(tasks0) { it -> it.map { it.toTaskEntity() } }
                val tasks2 : LiveData<List<TaskReduxEntity>> = Transformations.map(tasks1) { TaskReduxEntity.createTree(it) }
                Either.Right(tasks2)
            }
            catch(e: Exception) {
                Log.e(TAG, "TaskRepo:DataBase:getTasksList:e:----------------------------------",e)
                Either.Left(Failure.Database())
            }
        }
        override fun getTaskDetails(id: Int): Either<Failure, LiveData<TaskEntity>> {
            return try {
                //val taskDb = db.dao().selectById(id)
                Log.e(TAG, "getTaskDetails:----------------------------------id=$id")
                val task0 = db.dao().selectByIdLive(id)
                val task1 = Transformations.map(task0) { it.toTaskEntity() }
                Either.Right(task1)
            }
            catch(e: Exception) {
                Log.e(TAG, "TaskRepo:DataBase:getTaskDetails:e:--------------------------------",e)
                Either.Left(Failure.Database())
            }
        }

        override fun saveTasksDetails(task: TaskEntity): Either<Failure, Unit>  {
            return try {
                Log.e(TAG, "saveTasksDetails:-0---------------------------------$task")
                if(task.id == Task.ID_NIL)
                    db.dao().insert(TaskTable(task))
                else
                    db.dao().update(TaskTable(task))
                Log.e(TAG, "saveTasksDetails:-1---------------------------------")
                Either.Right(Unit)
            }
            catch(e: Exception) {
                Log.e(TAG, "TaskRepo:DataBase:saveTasksDetails:e:------------------------------",e)
                Either.Left(Failure.Database())
            }
        }

        companion object {
            val TAG: String = DataBase::class.java.simpleName
        }
    }
}
