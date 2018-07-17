package com.cesoft.organizate2.repo

import android.arch.lifecycle.LiveData
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.util.exception.Failure
import com.cesoft.organizate2.util.functional.Either
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.util.LogInterface
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
    @Inject constructor(private val db: Database, private val log: LogInterface)
        : TaskRepo {

        //TODO: Use LiveData !!
        override fun getTasksList(): Either<Failure, List<TaskReduxEntity>> {
            return try {
                val tasksDb = db.dao().selectRedux()
                val tasks = tasksDb.map { it.toTaskEntity() }
                Either.Right(tasks)
            }
            catch(e: Exception) {
                log.e(TAG, "TaskRepo:DataBase:getTasksList:e:----------------------------------",e)
                Either.Left(Failure.Database())
            }
        }
        override fun getTaskDetails(id: Int): Either<Failure, TaskEntity> {
            return try {
                val taskDb = db.dao().selectById(id)
                if(taskDb != null)
                    Either.Right(taskDb.toTaskEntity())
                else
                    Either.Left(Failure.TaskIdNotFound())
            }
            catch(e: Exception) {
                log.e(TAG, "TaskRepo:DataBase:getTaskDetails:e:--------------------------------",e)
                Either.Left(Failure.Database())
            }
        }

        companion object {
            val TAG: String = DataBase::class.java.simpleName
        }
    }
}
