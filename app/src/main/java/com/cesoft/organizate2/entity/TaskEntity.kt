package com.cesoft.organizate2.entity

import com.cesoft.organizate2.util.extension.empty

/**
 * Created by ccasanova on 23/05/2018
 */
data class TaskEntity(
        val id: Int,
        val id_super: Int,
        val name: String,
        val description: String,
        val priority: Int,
        val limit: Int,
        val order: Int,
        val created: Int,
        val modified: Int) {

    companion object {
        val NONE = -1
        fun empty() = TaskEntity(
                NONE, NONE,
                String.empty(), String.empty(),
                NONE, NONE, NONE, NONE, NONE)
    }
}
