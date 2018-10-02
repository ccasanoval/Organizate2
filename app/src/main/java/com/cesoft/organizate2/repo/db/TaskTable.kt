package com.cesoft.organizate2.repo.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.cesoft.organizate2.entity.TaskEntity

/**
 * Created by ccasanova on 23/05/2018
 */
@Entity(tableName = TaskTableContract.TABLE_NAME)
data class TaskTable(
        @NonNull
        @PrimaryKey(autoGenerate = true)
        override val id: Int,
        override val idSuper: Int,
        override val name: String,
        override val level: Int,
        val description: String,
        val priority: Int,
        val limit: Long,
        val created: Long,
        val modified: Long)
    : TaskTableBase(id, idSuper, name, level) {

    //override val childs: ArrayList<TaskTable> = ArrayList()
    constructor(id: Int,
                idSuper: Int,
                name: String,
                level: Int,
                description: String,
                priority: Int,
                limit: Long,
                created: Long,
                modified: Long,
                childs: ArrayList<TaskTable>)
            : this(id, idSuper, name, level, description, priority, limit, created, modified) {
        this.childs.clear()
        this.childs.addAll(childs)
    }

    constructor(task: TaskEntity)
            : this(task.id, task.idSuper, task.name, task.level, task.description, task.priority,
            task.limit, task.created, task.modified) {
        this.childs.clear()
        this.childs.addAll(childs)
    }

    fun toTaskEntity(): TaskEntity {
        return TaskEntity(id, idSuper, name, level, description, priority, limit,
                created, modified, childs.map { it -> (it as TaskTable).toTaskEntity() })
    }
}