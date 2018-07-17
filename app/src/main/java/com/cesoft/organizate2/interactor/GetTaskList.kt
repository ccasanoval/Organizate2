package com.cesoft.organizate2.interactor

import android.arch.lifecycle.LiveData
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.repo.TaskRepo
import javax.inject.Inject

/**
 * Created by ccasanova on 24/05/2018
 */
class GetTaskList
@Inject constructor(private val repo: TaskRepo)
    : UseCase<List<TaskReduxEntity>, UseCase.None>() {

    override suspend fun run(params: None) = repo.getTasksList()
}