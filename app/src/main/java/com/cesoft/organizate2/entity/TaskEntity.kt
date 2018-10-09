package com.cesoft.organizate2.entity

import com.cesoft.organizate2.util.extension.None
import java.util.*

/**
* Created by ccasanova on 23/05/2018
*/
//TODO: Validation...
data class TaskEntity(
        val id: Int,
        val idSuper: Int,
        val name: String,
        val level: Int,
        val description: String,
        val priority: Int,
        val limit: Date,
        val created: Date,
        val modified: Date,
        val childs: List<TaskEntity>) {

    fun toTaskReduxEntity() : TaskReduxEntity {
        return TaskReduxEntity(id, idSuper, name, level)
    }

    /*constructor(id: Int,
                idSuper: Int,
                name: String,
                level: Int,
                childs: ArrayList<TaskEntity>)
            : this(id, idSuper, name, level, "",
            0, Date(0), Date(0), Date(0), childs)*/


    companion object {
        val ID_NIL = 0//Int.None
        val NO_SUPER = 0//Int.None
        const val LEVEL1 = 0
        const val LEVEL2 = 1
        const val LEVEL3 = 2

        fun levelChildOf(level: Int): Int {
            return level + 1
        }

        val None = TaskEntity(
                ID_NIL, NO_SUPER,
                String.None, Int.None,
                String.None, Int.None,
                Date(0),Date(0),Date(0),
                //Long.None, Long.None, Long.None,
                listOf())
    }
}
