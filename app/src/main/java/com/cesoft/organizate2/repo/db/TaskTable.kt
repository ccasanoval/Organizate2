package com.cesoft.organizate2.repo.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.cesoft.organizate2.entity.TaskEntity

/**
 * Created by ccasanova on 23/05/2018
 */
@Entity(tableName = TaskTableContract.TABLE_NAME) //--> Then change table name in DAO
data class TaskTable(
        @NonNull
        @PrimaryKey
        override val id: Int,
        override val idSuper: Int,
        override val name: String,
        val description: String,
        val priority: Int,
        override val ordering: Int,
        val limit: Long,
        val created: Long,
        //val throw_test_exception: Int,
        val modified: Long)
    : TaskTableBase(id, idSuper, name, ordering) {

    //override val childs: ArrayList<TaskTable> = ArrayList()
    constructor(id: Int,
                idSuper: Int,
                name: String,
                description: String,
                priority: Int,
                ordering: Int,
                limit: Long,
                created: Long,
                modified: Long,
                childs: ArrayList<TaskTable>)
            : this(id, idSuper, name, description, priority, ordering, limit, created, modified) {
        this.childs.clear()
        this.childs.addAll(childs)
    }

    fun toTaskEntity(): TaskEntity {
        return TaskEntity(id, idSuper, name, description, priority, ordering, limit,
                created, modified, childs.map { it -> (it as TaskTable).toTaskEntity() })
    }

    /*fun filterChilds(tasks: List<TaskTable>) : List<TaskTable> {
        return tasks.filter { task -> task.idSuper == id }//.map { task -> task.id }
    }
    fun initChilds(tasks: List<TaskTable>) {
        childs.addAll(filterChilds(tasks))
    }*/


    //override fun toString() = "{TaskTable: {id:"+id+", id_padre:"+idSuper+", name:"+name+", description:"+description+", priority:"+priority+", order:"+ordering+", limit:"+limit+"} }"

    /*fun getId() = id
    fun getIdSuper() = idSuper
    fun getName() = name
    fun getDescription() = description
    fun getPriority() = priority*/
}