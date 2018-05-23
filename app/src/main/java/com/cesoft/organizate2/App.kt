package com.cesoft.organizate2

import android.app.Application
import com.cesoft.organizate2.util.di.AppComponent
import com.cesoft.organizate2.util.di.AppModule
import com.cesoft.organizate2.util.di.DaggerAppComponent

/**
 * Created by ccasanova on 23/05/2018
 */

class App : Application() {

    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
    }

    private fun injectMembers() = appComponent.inject(this)

    private fun initializeLeakDetection() {
        //if (BuildConfig.DEBUG) LeakCanary.install(this)
    }
}