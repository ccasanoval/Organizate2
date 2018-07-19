package com.cesoft.organizate2.interactor

import android.arch.lifecycle.LiveData
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.repo.TaskRepo
import javax.inject.Inject

/**
 * Created by ccasanova on 24/05/2018
 */
class GetTaskDetails
@Inject constructor(private val repo: TaskRepo)
    : UseCase<LiveData<TaskEntity>, Int>() {

    override suspend fun run(params: Int) = repo.getTaskDetails(params)
}