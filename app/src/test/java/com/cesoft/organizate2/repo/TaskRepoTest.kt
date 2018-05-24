package com.cesoft.organizate2.repo

import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.repo.db.TaskDao
import com.cesoft.organizate2.repo.db.TaskReduxTable
import com.cesoft.organizate2.util.extension.None
import com.cesoft.organizate2.util.functional.Either
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
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
/*
    @Test
    fun `movies service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val movies = taskRepoDB.movies()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `movies service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val movies = taskRepoDB.movies()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `movies service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)

        val movies = taskRepoDB.movies()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test
    fun `movies request should catch exceptions`() {
        given { networkHandler.isConnected }.willReturn(true)

        val movies = taskRepoDB.movies()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test
    fun `should return empty movie details by default`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { movieDetailsResponse.body() }.willReturn(null)
        given { movieDetailsResponse.isSuccessful }.willReturn(true)
        given { movieDetailsCall.execute() }.willReturn(movieDetailsResponse)
        given { service.movieDetails(1) }.willReturn(movieDetailsCall)

        val movieDetails = taskRepoDB.movieDetails(1)

        movieDetails shouldEqual Right(MovieDetails.empty())
        verify(service).movieDetails(1)
    }

    @Test
    fun `should get movie details from service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { movieDetailsResponse.body() }.willReturn(
                MovieDetailsEntity(8, "title", String.empty(), String.empty(),
                        String.empty(), String.empty(), 0, String.empty()))
        given { movieDetailsResponse.isSuccessful }.willReturn(true)
        given { movieDetailsCall.execute() }.willReturn(movieDetailsResponse)
        given { service.movieDetails(1) }.willReturn(movieDetailsCall)

        val movieDetails = taskRepoDB.movieDetails(1)

        movieDetails shouldEqual Right(MovieDetails(8, "title", String.empty(), String.empty(),
                String.empty(), String.empty(), 0, String.empty()))
        verify(service).movieDetails(1)
    }

    @Test
    fun `movie details service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val movieDetails = taskRepoDB.movieDetails(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `movie details service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val movieDetails = taskRepoDB.movieDetails(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `movie details service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)

        val movieDetails = taskRepoDB.movieDetails(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test
    fun `movie details request should catch exceptions`() {
        given { networkHandler.isConnected }.willReturn(true)

        val movieDetails = taskRepoDB.movieDetails(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }
*/
}