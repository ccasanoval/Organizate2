package com.cesoft.organizate2.entity

import com.cesoft.organizate2.util.extension.None

/**
* Created by ccasanova on 23/05/2018
*/
data class TaskEntity(
        val id: Int,
        val idSuper: Int,
        val name: String,
        val description: String,
        val priority: Int,
        val limit: Int,
        val order: Int,
        val created: Int,
        val modified: Int) {

    companion object {
        val none = TaskEntity(
                Int.None, Int.None,
                String.None, String.None,
                Int.None, Int.None, Int.None, Int.None, Int.None)
    }
}
