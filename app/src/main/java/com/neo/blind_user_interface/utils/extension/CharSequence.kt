package com.neo.blind_user_interface.utils.extension

import timber.log.Timber

fun <T : CharSequence> T?.ifEmptyOrNull(
    fallback: () -> T
): T {
    return if (this.isNullOrEmpty()) {
        fallback()
    } else {
        this
    }
}

fun <T : List<CharSequence?>> T.filterNotNullOrEmpty() = filterNot { it.isNullOrEmpty() }

infix fun CharSequence.`is`(childClass: Class<*>): Boolean {
    if (equals(childClass.name)) return true

    return runCatching {
        childClass.isAssignableFrom(Class.forName(toString()))
    }.onFailure {
        Timber.e(it)
    }.getOrElse {
        false
    }
}

fun CharSequence?.isNotNullOrEmpty(): Boolean {
    return !isNullOrEmpty()
}