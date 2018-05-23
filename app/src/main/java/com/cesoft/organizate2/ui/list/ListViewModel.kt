package com.cesoft.organizate2.ui.list

import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.repo.Database
import com.cesoft.organizate2.ui.base.BaseViewModel
import javax.inject.Inject
import android.arch.lifecycle.LiveData



/**
 * Created by ccasanova on 23/05/2018
 */
class ListViewModel @Inject constructor(private val db: Database) : BaseViewModel() {

    private val tasks: MutableLiveData<List<TaskEntity>> = MutableLiveData()

    fun loadTask() {
        Thread {
            val listDb = db.dao().select()
            //tasks.value = listDb.map { it.toTaskEntity() }
            tasks.postValue(listDb.map { it.toTaskEntity() })
        }.start()
    }

    fun getTasks(): LiveData<List<TaskEntity>> = tasks
}