package com.cesoft.organizate2.ui.item

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.entity.Task
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.ui.base.BaseViewModel
import com.cesoft.organizate2.util.Log
import com.cesoft.organizate2.interactor.GetTaskDetails
import com.cesoft.organizate2.interactor.GetTaskList
import com.cesoft.organizate2.interactor.UseCase
import javax.inject.Inject


/**
 * Created by ccasanova on 17/08/2018
 */
class ItemViewModel @Inject constructor(private val getTasks: GetTaskList, private val getTask: GetTaskDetails) : BaseViewModel() {

    private var isNew : Boolean = true
    //private var taskId: Int = TaskEntity.ID_NIL

    private var task: LiveData<TaskEntity>? = null
    private var tasks: LiveData<List<TaskReduxEntity>>? = null//TODO:Needed?
    private val taskReady: MutableLiveData<Boolean> = MutableLiveData()
    private val tasksReady: MutableLiveData<Boolean> = MutableLiveData()//TODO:Needed?
    private val parentName: MutableLiveData<String> = MutableLiveData()
    fun getTask(): LiveData<TaskEntity>? = task
    fun getTasks(): LiveData<List<TaskReduxEntity>>? = tasks
    fun getTaskReady(): LiveData<Boolean> = taskReady
    fun getTasksReady(): LiveData<Boolean> = tasksReady
    //fun getParentName(): LiveData<String> = parentName

    fun setIdSuper(idSuper: Int) {
        //task?.value.let { it.idSuper = idSuper }
        Log.e(TAG, "setIdSuper:------------------------------------------------$idSuper")
    }


    private fun handleTask(task: LiveData<TaskEntity>) {
        Log.e(TAG, "handleTask:----------------------------------------------------"+task.value)
        this.task = task
        this.taskReady.value = true
        loadTasks()
    }
    fun loadTask(taskId: Int) {
        Log.e(TAG, "loadTask:------------------------------------------------------$taskId")
        getTask.execute({ it.either(::handleFailure, ::handleTask) }, taskId)
    }
    private fun handleTasks(tasks: LiveData<List<TaskReduxEntity>>) {
        Log.e(TAG, "handleTasks:-------------------------------------------------"+tasks.value)
        this.tasks = tasks
        this.tasksReady.value = true
        //this.parentName.value = getNameById()
        Log.e(TAG, "handleTasks:-----------------------------parentName-----------------------${this.parentName.value}")
    }
    private fun loadTasks() {
        Log.e(TAG, "loadTasks:------------------------------------------------------")
        getTasks.execute({ it.either(::handleFailure, ::handleTasks) }, UseCase.None())
    }
    /*private fun handleTaskSuper(task: LiveData<TaskEntity>) {
        Log.e(TAG, "handleTaskSuper:------------------------------------------------------------"+task.value)
        this.taskSuper = task
        this.taskReady.value = true
    }
    fun loadTaskSuper(taskId: Int) {
        getTask.execute({ it.either(::handleFailure, ::handleTaskSuper) }, taskId)
    }*/
    fun getNameById() : String? {
        if(task?.value?.idSuper == Task.ID_NIL)return null
        task?.value?.idSuper?.let { id ->
            tasks?.value?.let {
                try {
                    return it
                            .filter { it.id == id }
                            .map { it.name }
                            .single()
                }
                catch(e: Exception) {
                    Log.e(TAG, "getNameById:e:-------id=$id-----------${it.size}", e)
                }
            }
        }
        return null
    }

    fun getTaskSupers() : Array<TaskReduxEntity>? {
        tasks?.value?.let {
            try {
                return it
                        .filter { it.level <= Task.LEVEL2 }//TODO: && it.id != this.task.id
                        .toTypedArray()
            }
            catch(e: Exception) {
                Log.e(TAG, "getTaskSupers:e:------------------${it.size}", e)
                return null
            }
        }
        return null
    }
    /* Solo superiores actuales : No puedes meter debajo de otro...
    fun getTaskSupers() : Array<TaskReduxEntity>? {
        task?.value?.level?.let { level ->
            tasks?.value?.let {
                try {
                    return it
                            .filter { it.level < level }
                            .toTypedArray()
                }
                catch(e: Exception) {
                    Log.e(TAG, "getTaskSupers:e:------------------${it.size}", e)
                    return null
                }
            }
        }
        return null
    }*/


    fun newTask() {
        isNew = true
    }

    companion object {
        val TAG = ItemViewModel::class.java.simpleName!!
    }
}