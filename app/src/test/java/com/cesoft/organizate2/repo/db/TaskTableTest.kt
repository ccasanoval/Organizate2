package com.cesoft.organizate2.repo.db

import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.util.extension.None
import org.amshove.kluent.shouldEqual
import org.junit.Test

/**
 * Created by ccasanova on 24/05/2018
 */
class TaskTableTest {

    @Test
    fun toTaskEntityTest() {
        val now = System.currentTimeMillis()
        val taskDb1 = TaskTable(1, Int.None, "Tarea 1", "Descripción 1",
                1, 3, now, 12345, 543210)
        val taskDb2 = TaskTable(1, Int.None, "Tarea 1", "Descripción 1",
                1, 3, now, 12345, 543210)
        val task   = TaskEntity(1, Int.None, "Tarea 1", "Descripción 1",
                1, 3, now, 12345, 543210, listOf())

        taskDb2 shouldEqual taskDb1
        task shouldEqual taskDb1.toTaskEntity()
    }

    @Test
    fun toTaskEntityTest2() {
        val now = System.currentTimeMillis()
        val idParent = 1
        val tasks = ArrayList<TaskTable>()
        for(id in 10..15) {
            tasks.add(TaskTable(id, idParent, "Task $id","Description $id",1, id, now, now, now))
        }

        val taskDb1 = TaskTable(idParent, Int.None, "Tarea 1", "Descripción 1",
                1, 3, now, 12345, 543210, tasks)
        val taskDb2 = TaskTable(idParent, Int.None, "Tarea 1", "Descripción 1",
                1, 3, now, 12345, 543210, tasks)
        val task   = TaskEntity(1, Int.None, "Tarea 1", "Descripción 1",
                1, 3, now, 12345, 543210, tasks.map(TaskTable::toTaskEntity))

        taskDb2 shouldEqual taskDb1
        task shouldEqual taskDb1.toTaskEntity()
    }

}