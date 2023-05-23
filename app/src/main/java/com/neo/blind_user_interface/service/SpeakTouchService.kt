package com.neo.blind_user_interface.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.neo.blind_user_interface.intercepter.FocusInterceptor
import com.neo.blind_user_interface.intercepter.SpeechInterceptor
import com.neo.blind_user_interface.intercepter.interfece.Interceptor
import com.neo.blind_user_interface.utils.extension.getInstance
import com.neo.blind_user_interface.utils.extension.getLog
import timber.log.Timber

class SpeakTouchService : AccessibilityService() {

    private val interceptors = mutableListOf<Interceptor>()

    override fun onCreate() {
        super.onCreate()

        interceptors.add(FocusInterceptor())
        interceptors.add(SpeechInterceptor.getInstance(this))
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Timber.d(event.getLog())

        interceptors.forEach { it.handle(event) }
    }

    override fun onDestroy() {
        super.onDestroy()

        interceptors
            .getInstance<SpeechInterceptor>()
            .shutdown()

        interceptors.clear()
    }

    override fun onInterrupt() = Unit
}
