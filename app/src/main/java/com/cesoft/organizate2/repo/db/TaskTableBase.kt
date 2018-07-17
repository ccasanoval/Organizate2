package com.cesoft.organizate2.repo.db

import android.arch.persistence.room.Ignore

/**
 * Created by ccasanova on 29/05/2018
 */
abstract class TaskTableBase(
        @Ignore open val id: Int,
        @Ignore open val idSuper: Int,
        @Ignore open val name: String,
        @Ignore open val level: Int) {

    @Ignore
    val childs: ArrayList<TaskTableBase> = ArrayList()

    fun filterChilds(tasks: List<TaskTableBase>) : List<TaskTableBase> {
        val list = tasks.filter { task -> task.idSuper == id && task.id != id }
        return list
    }
    fun initChilds(tasks: List<TaskTableBase>) {
        childs.clear()
        childs.addAll(filterChilds(tasks))
    }
}