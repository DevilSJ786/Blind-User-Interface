package com.neo.blind_user_interface.intercepter

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import com.neo.blind_user_interface.R
import com.neo.blind_user_interface.intercepter.interfece.Interceptor
import com.neo.blind_user_interface.model.Reader
import com.neo.blind_user_interface.model.UiText
import com.neo.blind_user_interface.utils.extension.getLog
import com.neo.blind_user_interface.utils.`typealias`.NodeInfo
import timber.log.Timber

class SpeechInterceptor(
    private val textToSpeech: TextToSpeech,
    private val context: Context,
    private val reader: Reader
) : Interceptor {

    override fun handle(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            speak(NodeInfo.wrap(event.source ?: return))
        }
    }

    private fun speak(text: CharSequence) {

        Timber.i("speak:  \"$text\"")

        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    private fun speak(text: UiText) {
        Timber.i("speak: $text")

        speak(text.resolved(context))
    }

    private fun speak(nodeInfo: NodeInfo) {

        Timber.i("speak: ${nodeInfo.getLog()}")

        speak(reader.getContent(nodeInfo))
    }

    fun shutdown() {

        Timber.i("shutdown")

        textToSpeech.shutdown()
    }

    companion object {

        fun getInstance(context: Context): SpeechInterceptor {

            var speechInterceptor: SpeechInterceptor? = null

            speechInterceptor = SpeechInterceptor(
                textToSpeech = TextToSpeech(context) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        speechInterceptor!!.speak(
                            UiText(
                                text = "%s %s",
                                UiText(R.string.app_name),
                                UiText(R.string.text_enabled)
                            ),
                        )
                    }
                },
                reader = Reader(context),
                context = context
            )

            return speechInterceptor
        }
    }
}