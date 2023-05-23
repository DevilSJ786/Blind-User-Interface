package com.neo.blind_user_interface.model

import android.content.Context
import androidx.annotation.StringRes

sealed interface UiText {

    val args: List<Any>

    data class Raw(
        val text: String,
        override val args: List<Any>
    ) : UiText

    data class Res(
        @StringRes val res: Int,
        override val args: List<Any>
    ) : UiText

    fun resolved(context: Context): String {
        val text = when (this) {
            is Raw -> text
            is Res -> context.getString(res)
        }

        if (args.isEmpty()) {
            return text
        }

        val args = resolvedArgs(context)
            .toTypedArray()

        return String.format(text, *args)
    }

    fun resolvedArgs(context: Context): List<Any> {
        return args.map { arg ->
            when (arg) {
                is UiText -> {
                    arg.resolved(context)
                }

                else -> arg
            }
        }
    }

    companion object {
        operator fun invoke(
            text: String,
            vararg args: Any
        ) = Raw(text, args.toList())

        operator fun invoke(
            @StringRes res: Int,
            vararg args: Any
        ) = Res(res, args.toList())
    }
}