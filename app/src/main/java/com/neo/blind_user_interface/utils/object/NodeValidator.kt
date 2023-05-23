package com.neo.blind_user_interface.utils.`object`

import com.neo.blind_user_interface.utils.extension.isNotNullOrEmpty
import com.neo.blind_user_interface.utils.extension.iterator
import com.neo.blind_user_interface.utils.`typealias`.NodeInfo

object NodeValidator {

    private fun isClickable(node: NodeInfo): Boolean {
        return node.isClickable || node.isLongClickable
    }

    private fun hasText(node: NodeInfo): Boolean {
        return listOf(
            node.text,
            node.hintText,
            node.contentDescription
        ).any { it.isNotNullOrEmpty() }
    }

    private fun isRequiredRead(node: NodeInfo): Boolean {
        return node.isFocusable && hasText(node)
    }

    private fun hasReadableChild(nodeInfo: NodeInfo): Boolean {
        for (child in nodeInfo) {
            if (isReadableAsChild(child)) return true
            if (hasReadableChild(child)) return true
        }

        return false
    }

    fun isValidForAccessible(node: NodeInfo): Boolean {
        return node.isImportantForAccessibility && node.isVisibleToUser
    }

    fun isRequiredFocus(node: NodeInfo): Boolean {
        return isRequiredRead(node) ||
                isClickable(node) &&
                hasReadableChild(node)
    }

    fun isReadable(node: NodeInfo): Boolean {
        return hasText(node) || node.isCheckable
    }

    fun isReadableAsChild(nodeInfo: NodeInfo): Boolean {
        return isReadable(nodeInfo) && !isRequiredFocus(nodeInfo)
    }
}