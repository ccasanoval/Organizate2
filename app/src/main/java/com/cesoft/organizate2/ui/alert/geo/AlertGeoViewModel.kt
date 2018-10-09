package com.cesoft.organizate2.ui.alert.geo

import com.cesoft.organizate2.interactor.GetAlertDate
import com.cesoft.organizate2.ui.base.BaseViewModel
import javax.inject.Inject

class AlertGeoViewModel @Inject constructor(
        private val getAlertDate: GetAlertDate
) : BaseViewModel()
{

}