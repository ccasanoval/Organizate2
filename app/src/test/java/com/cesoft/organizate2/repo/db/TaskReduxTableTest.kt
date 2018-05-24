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
        val taskDb = TaskReduxTable(1, Int.None, "Tarea 1", 10)
        val task = TaskReduxEntity(1, Int.None, "Tarea 1", 10)

        task shouldEqual taskDb.toTaskEntity()
    }
}