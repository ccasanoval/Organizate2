package com.cesoft.organizate2.util.di

import android.arch.persistence.room.Room
import android.content.Context
import com.cesoft.organizate2.App
import com.cesoft.organizate2.repo.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by ccasanova on 23/05/2018
 */

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = app

    @Provides
    @Singleton
    fun provideRetrofit(): Database {
        return Room.databaseBuilder(app, Database::class.java, "organizate.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    /*@Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/android10/Sample-Data/master/Android-CleanArchitecture-Kotlin/")
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }*/
}
