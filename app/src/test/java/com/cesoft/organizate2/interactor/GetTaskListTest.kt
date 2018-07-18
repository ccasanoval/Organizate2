package com.cesoft.organizate2.interactor

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.repo.TaskRepo
import com.cesoft.organizate2.util.functional.Either
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by ccasanova on 24/05/2018
 */
@RunWith(MockitoJUnitRunner::class)
class GetTaskListTest {

    private lateinit var getTaskListInteractor: GetTaskList

    @Mock
    private lateinit var repo: TaskRepo

    @Before
    fun setUp() {
        getTaskListInteractor = GetTaskList(repo)
        val list = listOf(TaskReduxEntity.None)
        val mld = MutableLiveData<List<TaskReduxEntity>>()
        mld.value = list
        given { repo.getTasksList() }.willReturn(Either.Right(mld))
    }

    @Test
    fun `should get data from repository`() {
        runBlocking { getTaskListInteractor.run(UseCase.None()) }
        verify(repo).getTasksList()
        verifyNoMoreInteractions(repo)
    }
}