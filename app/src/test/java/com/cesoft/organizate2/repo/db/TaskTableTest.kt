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
        val taskDb = TaskTable(1, Int.None, "Tarea 1", "Descripción 1", 1, 123456789, 10, 12345, 543210)
        val task = TaskEntity(1, Int.None, "Tarea 1", "Descripción 1", 1, 123456789, 10, 12345, 543210)

        task shouldEqual taskDb.toTaskEntity()
    }
}