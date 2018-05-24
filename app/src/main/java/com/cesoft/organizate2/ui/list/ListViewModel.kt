package com.cesoft.organizate2.ui.list

import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.ui.base.BaseViewModel
import javax.inject.Inject
import android.arch.lifecycle.LiveData
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.interactor.GetTaskList
import com.cesoft.organizate2.interactor.UseCase


/**
 * Created by ccasanova on 23/05/2018
 */
class ListViewModel @Inject constructor(private val getTasks: GetTaskList) : BaseViewModel() {

    private val tasks: MutableLiveData<List<TaskReduxEntity>> = MutableLiveData()


    private fun handleTaskList(tasks: List<TaskReduxEntity>) {
        this.tasks.value = tasks//movies.map { TaskRedux(it.id, it.poster) }
    }
    fun loadTask() {
        getTasks.execute({ it.either(::handleFailure, ::handleTaskList) }, UseCase.None())
        /*Thread {
            val listDb = db.dao().select()
            //tasks.value = listDb.map { it.toTaskEntity() }
            tasks.postValue(listDb.map { it.toTaskEntity() })
        }.start()*/
        //tasks.value = db.dao().selectAsync().value
    }

    fun getTasks(): LiveData<List<TaskReduxEntity>> = tasks
}