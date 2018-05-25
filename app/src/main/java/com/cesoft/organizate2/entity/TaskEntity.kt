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
        val ordering: Int,
        val limit: Long,
        val created: Long,
        val modified: Long) {

    companion object {
        val None = TaskEntity(
                Int.None, Int.None,
                String.None, String.None,
                Int.None, Int.None,
                Long.None, Long.None, Long.None)
    }
}
