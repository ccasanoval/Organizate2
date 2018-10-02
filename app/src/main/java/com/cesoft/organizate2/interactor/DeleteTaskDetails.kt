package com.cesoft.organizate2.interactor

import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.repo.TaskRepo
import javax.inject.Inject

/**
 * Created by ccasanova on 02/10/2018
 */
class DeleteTaskDetails
@Inject constructor(private val repo: TaskRepo)
    : UseCase<Any, TaskEntity>() {

    override suspend fun run(params: TaskEntity) = repo.deleteTasksDetails(params)
}