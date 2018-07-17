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
        val level: Int,
        val childs: List<TaskReduxEntity>) {

    companion object {
        val LEVEL1 = 0
        val LEVEL2 = 1
        val LEVEL3 = 2
        val None = TaskReduxEntity(Int.None, Int.None, String.None, Int.None, listOf())

        fun filterByLevel(list: List<TaskReduxEntity>, level: Int): List<TaskReduxEntity> {
            return list.filter { task -> task.level == level }
        }
    }
}
