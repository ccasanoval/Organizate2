package com.cesoft.organizate2.util

import com.cesoft.organizate2.BuildConfig

/**
 * Created by ccasanova on 24/05/2018
 */
object Log {
    fun e(tag: String, msg: String, t: Throwable? = null) {
        if(BuildConfig.DEBUG)
            android.util.Log.e(tag, msg, t)
    }
    fun d(tag: String, msg: String) {
        if(BuildConfig.DEBUG)
            android.util.Log.d(tag, msg)
    }
}