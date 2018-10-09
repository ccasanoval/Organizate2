package com.cesoft.organizate2.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Text2Speech @Inject constructor(val context: Context){

    private var tts: TextToSpeech? = null
    fun hablar(texto: String) {
        init(context)
        val utteranceId = context.hashCode().toString() + ""
        //if(status == TextToSpeech.SUCCESS) {
        tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun init(c: Context) {
        if(tts == null)
            tts = TextToSpeech(c.applicationContext, TextToSpeech.OnInitListener { status ->
                if(status == TextToSpeech.SUCCESS)
                    tts!!.language = Locale.getDefault()//new Locale("es", "ES");Locale.forLanguageTag("ES")
                else
                    Log.e(TAG, "init:e: status = $status")
            })
    }

    companion object {
        private val TAG: String = Text2Speech::class.java.simpleName
    }
}