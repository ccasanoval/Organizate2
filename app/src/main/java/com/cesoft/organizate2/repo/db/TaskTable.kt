package com.cesoft.organizate2.repo.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.cesoft.organizate2.entity.TaskEntity

/**
 * Created by ccasanova on 23/05/2018
 */
@Entity(tableName = TaskTableContract.TABLE_NAME) //--> Then change table name in DAO
class TaskTable(
        @NonNull
        @PrimaryKey
        val id: Int,
        val idSuper: Int,
        val name: String,
        val description: String,
        val priority: Int,
        val ordering: Int,
        val limit: Long,
        val created: Long,
        //val throw_exception: Int,
        val modified: Long) {

    fun toTaskEntity() = TaskEntity(id, idSuper, name, description, priority, ordering, limit, created, modified)
    override fun toString() = "{TaskTable: {id:"+id+", id_padre:"+idSuper+", name:"+name+", description:"+description+", priority:"+priority+", order:"+ordering+", limit:"+limit+"} }"

    /*fun getId() = id
    fun getIdSuper() = idSuper
    fun getName() = name
    fun getDescription() = description
    fun getPriority() = priority*/
}