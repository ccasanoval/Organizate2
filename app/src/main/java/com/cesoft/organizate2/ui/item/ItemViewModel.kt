package com.cesoft.organizate2.ui.item

import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.entity.Task
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.ui.base.BaseViewModel
import com.cesoft.organizate2.util.Log
import com.cesoft.organizate2.interactor.GetTaskDetails
import com.cesoft.organizate2.interactor.GetTaskList
import com.cesoft.organizate2.interactor.SaveTaskDetails
import com.cesoft.organizate2.interactor.UseCase
import javax.inject.Inject


/**
 * Created by ccasanova on 17/08/2018
 */
class ItemViewModel @Inject constructor(
        private val getTasks: GetTaskList,
        private val getTask: GetTaskDetails,
        private val saveTask: SaveTaskDetails
) : BaseViewModel() {


    val finish: MutableLiveData<Boolean> = MutableLiveData()
    val task: MutableLiveData<TaskEntity> = MutableLiveData()
    val tasks: MutableLiveData<List<TaskReduxEntity>> = MutableLiveData()
    private val parentName: MutableLiveData<String> = MutableLiveData()

    private var idSuper: Int = Task.NO_SUPER
    private var level: Int = Task.LEVEL1
    private var limit: Long = 0//TODO:

    fun setIdSuper(idSuper: Int) {
        this.idSuper = idSuper
        Log.e(TAG, "setIdSuper:------------------------------------------------: $idSuper")
    }
    fun setLevel(level: Int) {
        this.level = level
        Log.e(TAG, "level:------------------------------------------------: $level")
    }

    private fun handleTask(task: TaskEntity) {
        Log.e(TAG, "handleTask:----------------------------------------------------$task")
        this.task.value = task
        loadTasks()
    }
    fun loadTask(taskId: Int) {
        Log.e(TAG, "loadTask:------------------------------------------------------ $taskId")
        getTask.execute({ it.either(::handleFailure, ::handleTask) }, taskId)
    }
    private fun handleTasks(tasks: List<TaskReduxEntity>) {
        Log.e(TAG, "handleTasks:-------------------------------------------------$tasks")
        this.tasks.value = tasks
        Log.e(TAG, "handleTasks:-----------------------------parentName-----------------------${this.parentName.value}")
    }
    private fun loadTasks() {
        Log.e(TAG, "loadTasks:------------------------------------------------------")
        getTasks.execute({ it.either(::handleFailure, ::handleTasks) }, UseCase.None())
    }

    fun getNameById() : String? {
        if(task.value?.idSuper == Task.ID_NIL)return null
        task.value?.idSuper?.let { id ->
            tasks.value?.let { it ->
                try {
                    return it
                            .filter { it.id == id }
                            .map { it.name }
                            .single()
                }
                catch(e: Exception) {
                    Log.e(TAG, "getNameById:e:-------id=$id----------- ${it.size}", e)
                }
            }
        }
        return null
    }

    fun getTaskSupers() : Array<TaskReduxEntity>? {
        tasks.value?.let { it ->
            return try {
                it
                        .filter { it.level < Task.LEVEL3 }
                        .toTypedArray()
            }
            catch(e: Exception) {
                Log.e(TAG, "getTaskSupers:e:------------------ ${it.size}", e)
                null
            }
        }
        return null
    }

    fun newTask() {
        loadTasks()
    }

    fun save(name: String, description: String, priority: Int) {
        val newTask: TaskEntity?
        if(task.value != null) {
            newTask = TaskEntity(
                    task.value!!.id,
                    idSuper,
                    name,
                    task.value!!.level,
                    description,
                    priority,
                    task.value!!.limit,
                    task.value!!.created,
                    java.util.Date().time,
                    List(0) {TaskEntity.None})
        }
        else {
            newTask = TaskEntity(
                    Task.ID_NIL,
                    idSuper,
                    name,
                    level,
                    description,
                    priority,
                    limit,//TODO:
                    java.util.Date().time,
                    java.util.Date().time,
                    List(0) {TaskEntity.None})
        }

        Log.e(TAG, "SAVE:------$priority--------------------$newTask ")
        saveTask.execute({it ->
            it.either(::handleFailure){finish.value = true; Unit}
        }, newTask)
    }

    companion object {
        val TAG: String = ItemViewModel::class.java.simpleName
    }
}