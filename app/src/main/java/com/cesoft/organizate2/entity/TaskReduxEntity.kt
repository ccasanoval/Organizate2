package com.cesoft.organizate2.entity

import com.cesoft.organizate2.util.extension.None

/**
 * Created by ccasanova on 23/05/2018
 */
data class TaskReduxEntity(
        val id: Int,
        val idSuper: Int,
        val name: String,
        val ordering: Int,
        val childs: List<TaskReduxEntity>) {

    companion object {
        val None = TaskReduxEntity(Int.None, Int.None, String.None, Int.None, listOf())
    }
}
