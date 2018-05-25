package com.cesoft.organizate2.util

import com.cesoft.organizate2.BuildConfig

/**
 * Created by ccasanova on 24/05/2018
 */
interface LogInterface {
    fun e(tag: String, msg: String, t: Throwable? = null)
    fun d(tag: String, msg: String)

    class Log : LogInterface {
        override fun e(tag: String, msg: String, t: Throwable?) {
            if(BuildConfig.DEBUG)
                android.util.Log.e(tag, msg, t)
        }
        override fun d(tag: String, msg: String) {
            if(BuildConfig.DEBUG)
                android.util.Log.d(tag, msg)
        }
    }
}
