package com.cesoft.organizate2.ui.alert.date

import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.entity.AlertDateEntity
import com.cesoft.organizate2.interactor.GetAlertDate
import com.cesoft.organizate2.ui.base.BaseViewModel
import javax.inject.Inject

class AlertDateViewModel @Inject constructor(
        private val getAlertDate: GetAlertDate
) : BaseViewModel() {

    var isDirty: Boolean = false

    val data: MutableLiveData<AlertDateEntity> = MutableLiveData()
    val minutes
        get() = data.value?.minutes
    val hours
        get() = data.value?.hours
    val daysOfMonth
        get() = data.value?.daysOfMonth
    val daysOfWeek
        get() = data.value?.daysOfWeek
    val months
        get() = data.value?.months



    fun loadAlert(idTask: Int) {
        getAlertDate.execute({ it.either(::handleFailure, ::handleData) }, idTask)
    }
    private fun handleData(data: AlertDateEntity) {
        this.data.value = data
    }

    fun isNew() {

    }

    companion object {
        val TAG: String = AlertDateViewModel::class.java.simpleName
    }
}