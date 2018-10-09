package com.cesoft.organizate2.ui.alert.geo

import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import com.cesoft.organizate2.App
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.util.Text2Speech
import com.cesoft.organizate2.util.di.AppComponent
import javax.inject.Inject

class AlertGeoActivity : AppCompatActivity() {
        private val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
            (application as App).appComponent
        }

        @Inject
        lateinit var viewModelFactory: ViewModelProvider.Factory
        @Inject
        lateinit var db: Database
        @Inject
        lateinit var text2Speech: Text2Speech

        private lateinit var viewModel: AlertGeoViewModel


    }