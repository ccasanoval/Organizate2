package com.cesoft.organizate2.repo.db

import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.util.extension.None
import org.amshove.kluent.shouldEqual
import org.junit.Test

/**
 * Created by ccasanova on 24/05/2018
 */
class TaskReduxTableTest {

    @Test
    fun toTaskEntityTest() {
        val taskDb1 = TaskReduxTable(1, Int.None, "Tarea 1", 10)
        val taskDb2 = TaskReduxTable(1, Int.None, "Tarea 1", 10)
        val task = TaskReduxEntity(1, Int.None, "Tarea 1", 10, listOf())

        taskDb2 shouldEqual taskDb1
        task shouldEqual taskDb1.toTaskEntity()
    }

    @Test
    fun toTaskEntityTest2() {
        val idParent = 1
        val tasks = ArrayList<TaskReduxTable>()
        for(id in 10..15) {
            tasks.add(TaskReduxTable(id, idParent, "Task $id",1))
        }

        val taskDb1 = TaskReduxTable(idParent, Int.None, "Tarea 1", 1, tasks)
        val taskDb2 = TaskReduxTable(idParent, Int.None, "Tarea 1", 1, tasks)
        val task   = TaskReduxEntity(1, Int.None, "Tarea 1", 1, tasks.map(TaskReduxTable::toTaskEntity))

        taskDb2 shouldEqual taskDb1
        task shouldEqual taskDb1.toTaskEntity()
    }
}