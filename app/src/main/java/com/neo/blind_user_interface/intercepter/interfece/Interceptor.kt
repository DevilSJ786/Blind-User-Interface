package com.neo.blind_user_interface.intercepter.interfece

import android.view.accessibility.AccessibilityEvent

interface Interceptor {
    fun handle(event: AccessibilityEvent)
}
