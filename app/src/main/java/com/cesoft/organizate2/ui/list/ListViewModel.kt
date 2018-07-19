package com.cesoft.organizate2.ui.list

import com.cesoft.organizate2.ui.base.BaseViewModel
import javax.inject.Inject
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.interactor.GetTaskList
import com.cesoft.organizate2.interactor.UseCase


/**
 * Created by ccasanova on 23/05/2018
 */
class ListViewModel @Inject constructor(private val getTasks: GetTaskList) : BaseViewModel() {

    private var view: ListViewInterface? = null

    private var tasks: LiveData<List<TaskReduxEntity>>? = null
    private val tasksReady: MutableLiveData<Boolean> = MutableLiveData()
        fun getTasks(): LiveData<List<TaskReduxEntity>>? = tasks
        fun getTasksReady(): LiveData<Boolean> = tasksReady

    private fun handleTaskList(tasks: LiveData<List<TaskReduxEntity>>) {
        //Log.e(TAG, "handleTaskList:------------------------------------------------------------"+tasks.value?.size)
        this.tasks = tasks
        this.tasksReady.value = true
    }
    fun loadTask() {
        getTasks.execute({ it.either(::handleFailure, ::handleTaskList) }, UseCase.None())
    }




    fun onAddTask() {
        //TODO
        Log.e(TAG, "onAddTask:------------------------------------------------------------")
    }


    fun setView(view: ListViewInterface) {
        this.view = view
    }
    fun onClickTask(taskId: Int) {//task: TaskReduxEntity) {
        Log.e(TAG, "onClickN1:------------------------------------------------------------")
        view?.startActivity(taskId)
    }
    /*fun onClickN2(task: TaskReduxEntity) {
        Log.e(TAG, "onClickN2:------------------------------------------------------------")
        view?.startActivity(task)
    }
    fun onClickN3(task: TaskReduxEntity) {
        Log.e(TAG, "onClickN3:------------------------------------------------------------")
    }*/

    companion object {
        val TAG = ListViewModel::class.java.simpleName!!
    }
}