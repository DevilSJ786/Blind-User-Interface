package com.neo.blind_user_interface.utils.extension

inline fun <reified T> Iterable<Any>.getInstance(): T {
    return first { it is T } as T
}
