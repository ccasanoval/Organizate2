package com.cesoft.organizate2.entity

import com.cesoft.organizate2.util.extension.None

/**
 * Created by ccasanova on 23/05/2018
 */
data class TaskReduxEntity(
        val id: Int,
        val idSuper: Int,
        val name: String,
        val order: Int) {

    companion object {
        val none = TaskReduxEntity(Int.None, Int.None, String.None, Int.None)
    }
}
