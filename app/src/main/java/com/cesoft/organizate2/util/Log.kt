package com.cesoft.organizate2.util

import android.os.Build
import com.cesoft.organizate2.BuildConfig

/**
 * Created by ccasanova on 24/05/2018
 */
class Log {
    companion object {
        fun e(tag: String, msg: String, t: Throwable? = null) {
            if(Build.MODEL == null || Build.MODEL.toLowerCase().contains("sdk"))
                System.err.println("$tag : $msg : $t")
            else if(BuildConfig.DEBUG)
                android.util.Log.e(tag, msg, t)
        }
        fun d(tag: String, msg: String) {
            if(Build.MODEL == null || Build.MODEL.toLowerCase().contains("sdk"))
                System.err.println("$tag : $msg")
            else if(BuildConfig.DEBUG)
                android.util.Log.d(tag, msg)
        }
    }
}
