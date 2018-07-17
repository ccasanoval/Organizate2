package com.cesoft.organizate2.ui.list

import android.app.Activity
import com.cesoft.organizate2.ui.base.BaseViewModel
import javax.inject.Inject
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.util.Log
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.interactor.GetTaskList
import com.cesoft.organizate2.interactor.UseCase


/**
 * Created by ccasanova on 23/05/2018
 */
class ListViewModel @Inject constructor(private val getTasks: GetTaskList) : BaseViewModel() {

    private val tasksReady: MutableLiveData<Boolean> = MutableLiveData()
    private var tasks: LiveData<List<TaskReduxEntity>>? = null

    private fun handleTaskList(tasks: LiveData<List<TaskReduxEntity>>) {
        Log.e(TAG, "handleTaskList:------------------------------------------------------------"+tasks.value?.size)
        this.tasks = tasks
        this.tasksReady.value = true
    }
    //act: Activity, obs: (TaskReduxEntity)
    fun loadTask() {
        Log.e(TAG, "loadTask:------------------------------------------------------------")
        getTasks.execute({ it.either(::handleFailure, ::handleTaskList) }, UseCase.None())
    }

    fun getTasks(): LiveData<List<TaskReduxEntity>>? = tasks
    fun getTasksReady(): LiveData<Boolean> = tasksReady


    fun onAddTask() {
        //TODO
        Log.e(TAG, "onAddTask:------------------------------------------------------------")
    }

    companion object {
        val TAG = ListViewModel::class.java.simpleName
    }
}