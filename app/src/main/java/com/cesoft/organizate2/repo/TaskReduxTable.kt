package com.cesoft.organizate2.repo

import com.cesoft.organizate2.entity.TaskReduxEntity

/**
 * Created by ccasanova on 24/05/2018
 */
data class TaskReduxTable(
        val id: Int,
        val idSuper: Int,
        val name: String,
        val ordering: Int) {

    fun toTaskEntity() = TaskReduxEntity(id, idSuper, name, ordering)
    override fun toString() = "{Task: {id:"+id+", id_padre:"+idSuper+", name:"+name+", order:"+ordering+"} }"
}