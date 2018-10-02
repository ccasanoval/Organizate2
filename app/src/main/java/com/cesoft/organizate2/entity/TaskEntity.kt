package com.cesoft.organizate2.entity

import android.arch.persistence.room.PrimaryKey
import com.cesoft.organizate2.util.extension.None

/**
* Created by ccasanova on 23/05/2018
*/
//TODO: Validation...
data class TaskEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val idSuper: Int,
        val name: String,
        val level: Int,
        val description: String,
        val priority: Int,
        val limit: Long,
        val created: Long,
        val modified: Long,
        val childs: List<TaskEntity>) {

    fun toTaskReduxEntity() : TaskReduxEntity {
        return TaskReduxEntity(id, idSuper, name, level)
    }

    companion object {
        val None = TaskEntity(
                Task.ID_NIL, Task.NO_SUPER,
                String.None, Int.None,
                String.None, Int.None,
                Long.None, Long.None, Long.None,
                listOf())
    }
}
