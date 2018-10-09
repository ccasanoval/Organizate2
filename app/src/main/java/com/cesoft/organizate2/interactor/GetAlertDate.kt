package com.cesoft.organizate2.interactor

import com.cesoft.organizate2.entity.AlertDateEntity
import com.cesoft.organizate2.repo.TaskRepo
import javax.inject.Inject


/**
 * Created by ccasanova on 08/10/2018
 */
class GetAlertDate
@Inject constructor(private val repo: TaskRepo)
    : UseCase<AlertDateEntity, Int>() {

    override suspend fun run(params: Int) = repo.getAlertDate(params)
}