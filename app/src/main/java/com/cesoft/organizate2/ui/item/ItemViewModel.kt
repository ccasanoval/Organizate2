package com.cesoft.organizate2.ui.item

import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.interactor.*
import com.cesoft.organizate2.ui.base.BaseViewModel
import com.cesoft.organizate2.util.Log
import java.util.*
import javax.inject.Inject


/**
 * Created by ccasanova on 17/08/2018
 */
class ItemViewModel @Inject constructor(
        private val getTasks: GetTaskList,
        private val getTask: GetTaskDetails,
        private val saveTask: SaveTaskDetails,
        private val deleteTask: DeleteTaskDetails
) : BaseViewModel() {

    var isDirty: Boolean = false
    val finish: MutableLiveData<Boolean> = MutableLiveData()
    val task: MutableLiveData<TaskEntity> = MutableLiveData()
    val tasks: MutableLiveData<List<TaskReduxEntity>> = MutableLiveData()
    //private val parentName: MutableLiveData<String> = MutableLiveData()

    val taskId: Int?
        get() = task.value?.id
    val taskName: String?
        get() = task.value?.name
    val taskDescription: String?
        get() = task.value?.description
    val taskPriority: Int?
        get() = task.value?.priority

    private var idSuper: Int = TaskEntity.NO_SUPER
    private var level: Int = TaskEntity.LEVEL1
    private var limit: Date = Date(0)


    fun setIdSuper(idSuper: Int) {
        this.idSuper = idSuper
    }
    fun setLevel(level: Int) {
        this.level = level
    }

    private fun handleTask(task: TaskEntity) {
        this.task.value = task
        loadTasks()
    }
    fun loadTask(id: Int) {
        getTask.execute({ it.either(::handleFailure, ::handleTask) }, id)
    }
    private fun handleTasks(tasks: List<TaskReduxEntity>) {
        this.tasks.value = tasks
    }
    private fun loadTasks() {
        getTasks.execute({ it.either(::handleFailure, ::handleTasks) }, UseCase.None())
    }

    fun getNameById() : String? {
        if(task.value?.idSuper == TaskEntity.ID_NIL)return null
        task.value?.idSuper?.let { id ->
            tasks.value?.let { it ->
                try {
                    return it
                            .asSequence()
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
                        .filter {
                            it.level < TaskEntity.LEVEL3
                            && it.id != task.value?.id
                            && !it.isChild(task.value)}
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

    fun delete() {
        task.value?.let { thisTask ->
            // Delete children
            val childs: List<TaskReduxEntity> = TaskReduxEntity.filterByParent(tasks.value!!, thisTask.toTaskReduxEntity())
            for(childTask in childs) {
                deleteTask.execute({ it ->
                    it.either({}) { finish.value = true; Unit }
                }, childTask.toTaskEntity())
            }
            // Delete task
            deleteTask.execute({ it ->
                it.either(::handleFailure) { finish.value = true; Unit }
            }, thisTask)
        }
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
                    Date(),
                    List(0) {TaskEntity.None})
        }
        else {
            newTask = TaskEntity(
                    TaskEntity.ID_NIL,
                    idSuper,
                    name,
                    level,
                    description,
                    priority,
                    limit,//TODO:
                    Date(),
                    Date(),
                    List(0) {TaskEntity.None})
        }

        //Log.e(TAG, "SAVE:------$priority--------------------$newTask ")
        saveTask.execute({it ->
            it.either(::handleFailure){finish.value = true; Unit}
        }, newTask)
    }

    fun setDirtyFlag(name: String, description: String, priority: Int) {
        isDirty = !task.value?.name.equals(name)
                || !task.value?.description.equals(description)
                || task.value?.priority != priority
                || task.value?.idSuper != idSuper
        //Log.e(TAG, "$isDirty-------name=$name, desc=$description, pri=$priority, sup=$idSuper  =  ${task.value}")
    }


    companion object {
        val TAG: String = ItemViewModel::class.java.simpleName
        /*const val NAME = 1
        const val DESCRIPTION = 2
        const val PRIORITY = 3
        const val SUPPER = 4*/
    }
}