package com.cesoft.organizate2.ui.item

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.ui.base.BaseViewModel
import com.cesoft.organizate2.util.Log
import com.cesoft.organizate2.interactor.GetTaskDetails
import javax.inject.Inject


/**
 * Created by ccasanova on 17/08/2018
 */
class ItemViewModel @Inject constructor(private val getTask: GetTaskDetails) : BaseViewModel() {

    private var isNew : Boolean = true
    //private var taskId: Int = TaskEntity.ID_NIL

    private var task: LiveData<TaskEntity>? = null
    private var tasks: LiveData<List<TaskReduxEntity>>? = null
    private val taskReady: MutableLiveData<Boolean> = MutableLiveData()
    private val tasksReady: MutableLiveData<Boolean> = MutableLiveData()
    fun getTask(): LiveData<TaskEntity>? = task
    fun getTasks(): LiveData<List<TaskReduxEntity>>? = tasks
    fun getTaskReady(): LiveData<Boolean> = taskReady
    fun getTasksReady(): LiveData<Boolean> = taskReady


    private fun handleTask(task: LiveData<TaskEntity>) {
        Log.e(TAG, "handleTask:------------------------------------------------------------"+task.value)
        this.task = task
        this.taskReady.value = true
    }
    fun loadTask(taskId: Int) {
        getTask.execute({ it.either(::handleFailure, ::handleTask) }, taskId)
    }
    /*private fun handleTaskSuper(task: LiveData<TaskEntity>) {
        Log.e(TAG, "handleTaskSuper:------------------------------------------------------------"+task.value)
        this.taskSuper = task
        this.taskReady.value = true
    }
    fun loadTaskSuper(taskId: Int) {
        getTask.execute({ it.either(::handleFailure, ::handleTaskSuper) }, taskId)
    }*/

    fun newTask() {
        isNew = true
    }

    companion object {
        val TAG = ItemViewModel::class.java.simpleName!!
    }
}