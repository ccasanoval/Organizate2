package com.cesoft.organizate2

import android.app.Application
import com.cesoft.organizate2.util.di.AppComponent
import com.cesoft.organizate2.util.di.AppModule
import com.cesoft.organizate2.util.di.DaggerAppComponent

/**
 * Created by ccasanova on 23/05/2018
 */
//TODO: Fingerprint and encrypting room db
//https://proandroiddev.com/secure-data-in-android-encryption-in-android-part-2-991a89e55a23
//https://proandroiddev.com/secure-data-in-android-encrypting-large-data-dda256a55b36

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