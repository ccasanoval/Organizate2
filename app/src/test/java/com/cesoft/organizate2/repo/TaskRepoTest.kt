package com.cesoft.organizate2.repo

import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.repo.db.TaskDao
import com.cesoft.organizate2.repo.db.TaskReduxTable
import com.cesoft.organizate2.util.exception.Failure
import com.cesoft.organizate2.util.extension.None
import com.cesoft.organizate2.util.functional.Either
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verifyZeroInteractions
import org.mockito.exceptions.base.MockitoException
import org.mockito.junit.MockitoJUnitRunner

//interactive
//https://medium.com/exploring-android/android-architecture-components-testing-your-room-dao-classes-e06e1c9a1535
//https://proandroiddev.com/testing-the-un-testable-and-beyond-with-android-architecture-components-part-1-testing-room-4d97dec0f451
/**
 * Created by ccasanova on 24/05/2018
 */
@RunWith(MockitoJUnitRunner::class)
class TaskRepoTest {

    private lateinit var taskRepoDB: TaskRepo.DataBase

    @Mock private lateinit var db: Database
    @Mock private lateinit var dao: TaskDao

    @Before
    fun setUp() {
        taskRepoDB = TaskRepo.DataBase(db)
        given { db.dao() }.willReturn(dao)
    }

    @Test
    fun `should return empty list by default`() {
        given { dao.selectRedux() }.willReturn( listOf<TaskReduxTable>() )
        val tasks = taskRepoDB.getTasksList()

        tasks shouldEqual Either.Right(emptyList<TaskReduxEntity>())
        verify(db).dao()
        verify(dao).selectRedux()
    }

    @Test
    fun `should get task list from database`() {
        val taskDb = TaskReduxTable(1, Int.None, "Tarea 1", 10)
        given { dao.selectRedux() }.willReturn( listOf(taskDb) )
        val tasks = taskRepoDB.getTasksList()

        //val task = TaskReduxEntity(1, Int.None, "Tarea 1", 10)
        tasks shouldEqual Either.Right(listOf(taskDb.toTaskEntity()))
        verify(db).dao()
        verify(dao).selectRedux()
    }

    @Test
    fun `task repo should return Failure if Database fails`() {
        given { dao.selectRedux() }.willThrow(MockitoException("test"))
        val tasks = taskRepoDB.getTasksList()

        tasks shouldBeInstanceOf Either::class.java
        tasks.isLeft shouldEqual true
        tasks.either({ failure -> failure shouldBeInstanceOf Failure.Database::class.java }, {})
    }

}