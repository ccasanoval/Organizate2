package com.cesoft.organizate2.repo

import com.cesoft.organizate2.entity.AlertDateEntity
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.util.exception.Failure
import com.cesoft.organizate2.util.functional.Either
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.repo.db.TaskTable
import com.cesoft.organizate2.util.Log
import javax.inject.Inject

/**
 * Created by ccasanova on 24/05/2018
 */
interface TaskRepo {
    fun getTasksList(): Either<Failure, List<TaskReduxEntity>>
    fun getTaskDetails(id: Int): Either<Failure, TaskEntity>
    fun saveTasksDetails(task: TaskEntity): Either<Failure, Unit>
    fun deleteTasksDetails(task: TaskEntity): Either<Failure, Unit>
    //
    fun getAlertDate(id: Int): Either<Failure, AlertDateEntity>

    ///---------------------------------------------------------------------------------------------
    //class Network

    ///---------------------------------------------------------------------------------------------
    class DataBase
    @Inject constructor(private val db: Database)
        : TaskRepo {

        override fun getTasksList(): Either<Failure, List<TaskReduxEntity>> {
            return try {
                val tasks0 = db.daoTask().selectRedux()
                val tasks1 : List<TaskReduxEntity> = tasks0.map { it.toTaskEntity() }
                val tasks2 : List<TaskReduxEntity> = TaskReduxEntity.createTree(tasks1)
                val tasks3 : List<TaskReduxEntity> = TaskReduxEntity.orderTree(tasks2)
                Either.Right(tasks3)
            }
            catch(e: Exception) {
                Log.e(TAG, "TaskRepo:DataBase:getTasksList:e:----------------------------------",e)
                Either.Left(Failure.Database())
            }
        }
        override fun getTaskDetails(id: Int): Either<Failure, TaskEntity> {
            return try {
                val task0 = db.daoTask().selectById(id)?.toTaskEntity() ?: return Either.Left(Failure.TaskIdNotFound())
                Either.Right(task0)
            }
            catch(e: Exception) {
                Log.e(TAG, "TaskRepo:DataBase:getTaskDetails:e:--------------------------------",e)
                Either.Left(Failure.Database())
            }
        }

        override fun saveTasksDetails(task: TaskEntity): Either<Failure, Unit> {
            return try {
                if(task.id == TaskEntity.ID_NIL)
                    db.daoTask().insert(TaskTable(task))
                else
                    db.daoTask().update(TaskTable(task))
                Either.Right(Unit)
            }
            catch(e: Exception) {
                Log.e(TAG, "TaskRepo:DataBase:saveTasksDetails:e:------------------------------",e)
                Either.Left(Failure.Database())
            }
        }

        override fun deleteTasksDetails(task: TaskEntity): Either<Failure, Unit> {
            return try {
                db.daoTask().delete(TaskTable(task))
                Either.Right(Unit)
            }
            catch(e: Exception) {
                Log.e(TAG, "TaskRepo:DataBase:deleteTasksDetails:e:----------------------------",e)
                Either.Left(Failure.Database())
            }
        }

        //------ ALERT DATE
        override fun getAlertDate(id: Int): Either<Failure, AlertDateEntity> {
            return try {
                val alert = db.daoAlertDate().selectById(id)?.toEntity() ?: return Either.Left(Failure.TaskIdNotFound())
                Either.Right(alert)
            }
            catch(e: Exception) {
                Log.e(TAG, "TaskRepo:DataBase:getAlertDate:e:--------------------------------",e)
                Either.Left(Failure.Database())
            }
        }


        //------
        companion object {
            val TAG: String = DataBase::class.java.simpleName
        }
    }
}
