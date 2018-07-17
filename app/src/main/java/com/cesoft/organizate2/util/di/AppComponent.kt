package com.cesoft.organizate2.util.di

import com.cesoft.organizate2.App
import com.cesoft.organizate2.ui.list.ListActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by ccasanova on 23/05/2018
 */
@Singleton
@Component(modules = [ AppModule::class, ViewModelModule::class ])
interface AppComponent {
    fun inject(app: App)
    fun inject(v: ListActivity)
}