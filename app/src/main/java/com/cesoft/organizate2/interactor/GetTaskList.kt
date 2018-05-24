package com.cesoft.organizate2.interactor

import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.repo.Database
import com.cesoft.organizate2.util.functional.Either
import javax.inject.Inject

/**
 * Created by ccasanova on 24/05/2018
 */
class GetTaskList
    @Inject constructor(private val repo: Database)
    : UseCase<List<TaskReduxEntity>, UseCase.None>() {

    override suspend fun run(params: None)
            = Either.Right( repo.dao().selectRedux().map { it.toTaskEntity() } )
}