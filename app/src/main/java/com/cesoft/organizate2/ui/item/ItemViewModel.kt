package com.cesoft.organizate2.ui.item

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.ui.base.BaseViewModel
import javax.inject.Inject


/**
 * Created by ccasanova on 17/08/2018
 */
class ItemViewModel @Inject constructor() : BaseViewModel() {

    private val tasksReady: MutableLiveData<Boolean> = MutableLiveData()
    private var tasks: LiveData<List<TaskReduxEntity>>? = null

    companion object {
        val TAG = ItemViewModel::class.java.simpleName!!
    }
}