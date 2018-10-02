package com.cesoft.organizate2.entity

import com.cesoft.organizate2.util.extension.None
import java.util.ArrayList

/**
 * Created by ccasanova on 23/05/2018
 */
data class TaskReduxEntity(
        val id: Int,
        val idSuper: Int,
        val name: String,
        val level: Int) {

    private var _childs: ArrayList<TaskReduxEntity>? = null

    var childs: List<TaskReduxEntity> = listOf()
        get() {
            return if(_childs?.toList() == null) listOf()
                else _childs!!.toList()
        }

    companion object {
        val None = TaskReduxEntity(Int.None, Int.None, String.None, Int.None)

        fun filterByLevel(list: List<TaskReduxEntity>, level: Int): List<TaskReduxEntity> {
            return list.filter { task -> task.level == level }
        }

        internal fun filterByParent(list: List<TaskReduxEntity>, parent: TaskReduxEntity): List<TaskReduxEntity> {
            return list.filter { task -> task.idSuper == parent.id && task.level > parent.level }
        }

        fun createTree(list: List<TaskReduxEntity>): List<TaskReduxEntity> {
            for(task in list)
                task._childs = ArrayList(filterByParent(list, task))
            return list
        }

        fun orderTree(list: List<TaskReduxEntity>): List<TaskReduxEntity> {
            val res = ArrayList<TaskReduxEntity>()
            for(task1 in list) {
                if(task1.level == Task.LEVEL1) {
                    res.add(task1)
                    for(task2 in task1.childs) {
                        res.add(task2)
                        for(task3 in task2.childs) {
                            res.add(task3)
                        }
                    }
                }
            }
            return res
        }
    }
}
