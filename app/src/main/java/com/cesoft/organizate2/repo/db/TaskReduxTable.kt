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
        override val level: Int)
    : TaskTableBase(id, idSuper, name, level)  {

    constructor(id: Int,
                idSuper: Int,
                name: String,
                level: Int,
                childs: ArrayList<TaskReduxTable>)
            : this(id, idSuper, name, level) {
        this.childs.clear()
        this.childs.addAll(childs)
    }

    fun toTaskEntity(): TaskReduxEntity {
        return TaskReduxEntity(id, idSuper, name, level)
    }

}