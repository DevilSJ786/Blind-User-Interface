package com.neo.blind_user_interface.utils.extension

import android.content.Context
import com.neo.blind_user_interface.R


fun Context.getText(isEnabled: Boolean): String {
    return if (isEnabled) {
        getString(R.string.text_enabled)
    } else {
        getString(R.string.text_disabled)
    }
}
