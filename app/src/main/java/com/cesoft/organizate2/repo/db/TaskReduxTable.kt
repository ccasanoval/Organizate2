package com.cesoft.organizate2.repo.db

//import android.arch.persistence.room.Ignore
import com.cesoft.organizate2.entity.TaskReduxEntity

/**
 * Created by ccasanova on 24/05/2018
 */
data class TaskReduxTable(
        override val id: Int,
        override val idSuper: Int,
        override val name: String,
        override val ordering: Int)
    : TaskTableBase(id, idSuper, name, ordering)  {

    //val childs: ArrayList<TaskReduxTable> = ArrayList()

    constructor(id: Int,
                idSuper: Int,
                name: String,
                ordering: Int,
                childs: ArrayList<TaskReduxTable>)
            : this(id, idSuper, name, ordering) {
        this.childs.clear()
        this.childs.addAll(childs)
    }
    constructor(task: TaskTable): this(task.id,task.idSuper,task.name,task.ordering) {
        this.childs.clear()
        val childs = task.childs.map { it -> TaskReduxTable(it as TaskTable) }
        this.childs.addAll(childs)
    }

    fun toTaskEntity(): TaskReduxEntity {
        return TaskReduxEntity(id, idSuper, name, ordering, childs.map{it -> (it as TaskReduxTable).toTaskEntity()})
    }

    /*fun filterChilds(tasks: List<TaskReduxTable>) : List<TaskReduxTable> {
        return tasks.filter { task -> task.idSuper == id }//.map { task -> task.id }
    }
    fun initChilds(tasks: List<TaskReduxTable>) {
        childs.addAll(filterChilds(tasks))
    }*/

    //override fun toString() = "{Task: {id:"+id+", id_padre:"+idSuper+", name:"+name+", order:"+ordering+"} }"
}