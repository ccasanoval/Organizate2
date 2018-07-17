package com.cesoft.organizate2.ui.list

import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.ui.base.BaseViewModel
import javax.inject.Inject
import android.arch.lifecycle.LiveData
import android.util.Log
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.interactor.GetTaskList
import com.cesoft.organizate2.interactor.UseCase


/**
 * Created by ccasanova on 23/05/2018
 */
class ListViewModel @Inject constructor(private val getTasks: GetTaskList) : BaseViewModel() {

    private val tasks: MutableLiveData<List<TaskReduxEntity>> = MutableLiveData()

    private fun handleTaskList(tasks: List<TaskReduxEntity>) {
        Log.e(TAG, "handleTaskList:------------------------------------------------------------"+tasks.size)
        this.tasks.value = tasks
    }
    fun loadTask() {
        Log.e(TAG, "loadTask:------------------------------------------------------------")
        getTasks.execute({ it.either(::handleFailure, ::handleTaskList) }, UseCase.None())
    }

    fun getTasks(): LiveData<List<TaskReduxEntity>> = tasks



    fun onAddTask() {
        //TODO
        Log.e(TAG, "onAddTask:------------------------------------------------------------")
    }

    companion object {
        val TAG = ListViewModel::class.java.simpleName
    }
}