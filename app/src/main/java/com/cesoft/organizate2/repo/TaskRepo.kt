package com.cesoft.organizate2.repo

import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.util.exception.Failure
import com.cesoft.organizate2.util.functional.Either
import com.cesoft.organizate2.repo.db.Database
import javax.inject.Inject

/**
 * Created by ccasanova on 24/05/2018
 */

interface TaskRepo {
    fun getTasksList(): Either<Failure, List<TaskReduxEntity>>
    fun getTaskDetails(id: Int): Either<Failure, TaskEntity>

    ///---------------------------------------------------------------------------------------------
    //class Network

    ///---------------------------------------------------------------------------------------------
    class DataBase
    @Inject constructor(private val db: Database) : TaskRepo {

        //TODO: Use LiveData !!
        override fun getTasksList(): Either<Failure, List<TaskReduxEntity>> {
            try {
                val tasksDb = db.dao().selectRedux()
                val tasks = tasksDb.map { it.toTaskEntity() }
                return Either.Right(tasks)
            }
            catch(e: Exception) {
                //Log.e(TAG, "TaskRepo:DataBase:getTasksList:e:----------------------------------",e)
                return Either.Left(Failure.Database())
            }
        }
        override fun getTaskDetails(id: Int): Either<Failure, TaskEntity> {
            val taskDb = db.dao().selectById(id)
            val task = taskDb.toTaskEntity()
            return Either.Right(task)
        }

        companion object {
            //val TAG: String = DataBase::class.java.simpleName
        }
    }
}
