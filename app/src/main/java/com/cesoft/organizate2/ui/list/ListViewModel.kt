package com.cesoft.organizate2.ui.list

import com.cesoft.organizate2.ui.base.BaseViewModel
import javax.inject.Inject
import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.interactor.GetTaskList
import com.cesoft.organizate2.interactor.UseCase


/**
 * Created by ccasanova on 23/05/2018
 */
class ListViewModel @Inject constructor(private val getTasks: GetTaskList) : BaseViewModel() {

    private var view: ListViewInterface? = null

    val tasks: MutableLiveData<List<TaskReduxEntity>> = MutableLiveData()

    private fun handleTaskList(tasks: List<TaskReduxEntity>) {
        //Log.e(TAG, "handleTaskList:------------------------------------------------------------"+tasks.value?.size)
        this.tasks.value = tasks
    }
    fun loadTask() {
        getTasks(UseCase.None) { it.either(::handleFailure, ::handleTaskList) }
    }


    fun onAddTask() {
        view?.startActivity(TaskEntity.ID_NIL)
        //Log.e(TAG, "onAddTask:------------------------------------------------------------")
    }


    fun setView(view: ListViewInterface) {
        this.view = view
    }
    fun onClickTask(taskId: Int) {
        //Log.e(TAG, "onClickN1:------------------------------------------------------------")
        view?.startActivity(taskId)
    }

    companion object {
        val TAG : String = ListViewModel::class.java.simpleName
    }
}