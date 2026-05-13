package com.up.projectmanager.util

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import java.util.Locale

class TTSManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var isEnabled = true

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isInitialized = true
            }
        }
    }

    fun speak(text: String) {
        if (isEnabled && isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }

    companion object {
        fun createTtsCheckIntent(): Intent {
            return Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA)
        }
    }
}
