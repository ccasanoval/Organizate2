package com.cesoft.organizate2.repo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.cesoft.organizate2.entity.TaskEntity

/**
 * Created by ccasanova on 23/05/2018
 */
@Entity//(tableName = "task") --> Then change table name in DAO
class TaskTable(
        @NonNull
        @PrimaryKey
        val id: Int,
        val idSuper: Int,
        val name: String,
        val description: String = "",
        val priority: Int = 0,
        val limit: Int = 0,
        val order: Int = 0,
        val created: Int = 0,
        val modified: Int = 0) {

    fun toTaskEntity() = TaskEntity(id, idSuper, name, description, priority, limit, order, created, modified)
    override fun toString() = "{Task: {id:"+id+", id_padre:"+idSuper+", name:"+name+", description:"+description+"} }"

    /*fun getId() = id
    fun getIdSuper() = idSuper
    fun getName() = name
    fun getDescription() = description
    fun getPriority() = priority*/

    companion object { }
}