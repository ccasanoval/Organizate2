package com.cesoft.organizate2.interactor

import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.repo.TaskRepo
import com.cesoft.organizate2.util.exception.Failure
import com.cesoft.organizate2.util.functional.Either
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import kotlinx.coroutines.experimental.runBlocking
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by ccasanova on 24/05/2018
 */
@RunWith(MockitoJUnitRunner::class)
class GetTaskDetailsTest {

    private lateinit var getTaskDetailsInteractor: GetTaskDetails

    @Mock
    private lateinit var repo: TaskRepo

    @Before
    fun setUp() {
        getTaskDetailsInteractor = GetTaskDetails(repo)
        given { repo.getTaskDetails(Mockito.anyInt()) }.willReturn(Either.Right(TaskEntity.None))
        //given { repo.getTaskDetails(1     ) }.willReturn(Either.Right(TaskEntity.None))
        given { repo.getTaskDetails(696969) }.willReturn(Either.Left(Failure.TaskIdNotFound()))
    }

    @Test
    fun `should get Failure if not find id`() {
        val id = 696969
        runBlocking {
            val res: Either<Failure, TaskEntity>
            res = getTaskDetailsInteractor.run(id)

            res shouldBeInstanceOf Either::class.java
            res.isLeft shouldEqual true
            res.either({ failure -> failure shouldBeInstanceOf Failure.TaskIdNotFound::class.java }, {})
        }
        verify(repo).getTaskDetails(id)
        verifyNoMoreInteractions(repo)
    }

    @Test
    fun `should get data from repository`() {
        val id = 1
        given { repo.getTaskDetails(Mockito.anyInt()) }.willReturn(Either.Right(TaskEntity.None))
        runBlocking {
            val res: Either<Failure, TaskEntity>
            res = getTaskDetailsInteractor.run(id)
            res shouldEqual Either.Right(TaskEntity.None)
        }
        verify(repo).getTaskDetails(id)
        verifyNoMoreInteractions(repo)
    }
}