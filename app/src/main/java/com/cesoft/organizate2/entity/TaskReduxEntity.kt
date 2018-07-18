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

    private var childs: ArrayList<TaskReduxEntity>? = null

    var Childs: List<TaskReduxEntity> = listOf()
        get() {
            return if(childs?.toList() == null) listOf()
                else childs!!.toList()
        }

    companion object {
        val LEVEL1 = 0
        val LEVEL2 = 1
        val LEVEL3 = 2
        val NO_SUPER = Int.None
        val None = TaskReduxEntity(Int.None, Int.None, String.None, Int.None)

        fun filterByLevel(list: List<TaskReduxEntity>, level: Int): List<TaskReduxEntity> {
            return list.filter { task -> task.level == level }
        }

        internal fun filterByParent(list: List<TaskReduxEntity>, parent: TaskReduxEntity): List<TaskReduxEntity> {
            return list.filter { task -> task.idSuper == parent.id && task.level > parent.level }
        }

        fun createTree(list: List<TaskReduxEntity>): List<TaskReduxEntity> {
            for(task in list)
                task.childs = ArrayList(filterByParent(list, task))
            return list
        }
    }
}
