package com.neo.blind_user_interface.model

import android.content.Context
import com.neo.blind_user_interface.R
import com.neo.blind_user_interface.utils.extension.filterNotNullOrEmpty
import com.neo.blind_user_interface.utils.extension.getLog
import com.neo.blind_user_interface.utils.extension.ifEmptyOrNull
import com.neo.blind_user_interface.utils.extension.iterator
import com.neo.blind_user_interface.utils.`object`.NodeValidator
import com.neo.blind_user_interface.utils.`typealias`.NodeInfo
import timber.log.Timber

class Reader(
    private val context: Context
) {
    fun getContent(
        nodeInfo: NodeInfo
    ) = getContent(
        nodeInfo,
        Level.Text(
            mustReadSelection = true,
            mustReadType = true,
            mustReadCheckable = true
        )
    )

    private fun getContent(
        node: NodeInfo,
        level: Level
    ): String {
        Timber.d(
            node.getLog(
                "level: $level"
            )
        )

        return when (level) {
            is Level.Text -> with(node) {
                val content = contentDescription.ifEmptyOrNull {
                    text.ifEmptyOrNull {
                        hintText.ifEmptyOrNull {
                            getContent(
                                node,
                                Level.Children
                            )
                        }
                    }
                }

                listOf(
                    content,
                    getType(node, level.mustReadType),
                    getCheckable(node, level.mustReadCheckable),
                    getSelection(node, level.mustReadSelection)
                ).filterNotNullOrEmpty()
            }

            is Level.Children -> buildList {
                for (child in node) {

                    if (!NodeValidator.isValidForAccessible(child)) continue
                    if (!NodeValidator.isReadableAsChild(child)) continue

                    add(
                        getContent(
                            child,
                            Level.Text(
                                mustReadSelection = false,
                                mustReadCheckable = true,
                                mustReadType = listOf(
                                    Type.SWITCH,
                                    Type.TOGGLE,
                                    Type.RADIO,
                                    Type.CHECKBOX
                                ).any { it == Type.get(child) }
                            )
                        )
                    )
                }
            }
        }.joinToString(", ")
    }

    private fun getType(
        node: NodeInfo,
        mustRead: Boolean
    ): String? {

        if (!mustRead) return null

        return when (Type.get(node)) {
            Type.NONE -> null
            Type.IMAGE -> context.getString(R.string.text_image_type)
            Type.SWITCH -> context.getString(R.string.text_switch_type)
            Type.TOGGLE -> context.getString(R.string.text_toggle_type)
            Type.RADIO -> context.getString(R.string.text_radio_type)
            Type.CHECKBOX -> context.getString(R.string.text_checkbox_type)
            Type.BUTTON -> context.getString(R.string.text_button_type)
            Type.EDITFIELD -> context.getString(R.string.text_editfield_type)
            Type.OPTIONS -> context.getString(R.string.text_options_type)
            Type.LIST -> context.getString(R.string.text_list_type)
            Type.TITLE -> context.getString(R.string.text_title_type)
        }
    }

    private fun getCheckable(
        nodeInfo: NodeInfo,
        mustRead: Boolean
    ): CharSequence? {
        if (!mustRead) return null
        if (!nodeInfo.isCheckable) return null

        return if (nodeInfo.isCheckable) {
            context.getString(R.string.text_enabled)
        } else {
            context.getString(R.string.text_enabled)
        }
    }

    private fun getSelection(
        node: NodeInfo,
        mustRead: Boolean
    ) = if (mustRead && node.isSelected) {
        context.getString(R.string.text_selected)
    } else {
        null
    }

    sealed interface Level {

        data class Text(
            val mustReadSelection: Boolean,
            val mustReadType: Boolean,
            val mustReadCheckable: Boolean
        ) : Level

        object Children : Level
    }
}