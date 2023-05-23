package com.neo.blind_user_interface.intercepter

import android.view.accessibility.AccessibilityEvent
import com.neo.blind_user_interface.intercepter.interfece.Interceptor
import com.neo.blind_user_interface.utils.`object`.NodeValidator
import com.neo.blind_user_interface.utils.`typealias`.NodeInfo
import com.neo.blind_user_interface.utils.extension.getNearestAncestor

class FocusInterceptor : Interceptor {

    override fun handle(event: AccessibilityEvent) {

        val nodeInfo = NodeInfo.wrap(event.source ?: return)

        if (nodeInfo.isAccessibilityFocused) return

        if (!NodeValidator.isValidForAccessible(nodeInfo)) return

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> {
                handlerAccessibilityNode(nodeInfo)
            }

            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                handlerAccessibilityNode(nodeInfo)
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                handlerAccessibilityNode(nodeInfo)
            }

            else -> Unit
        }
    }

    private fun handlerAccessibilityNode(nodeInfo: NodeInfo) {
        getFocusableNode(nodeInfo)?.run {
            performAction(NodeInfo.ACTION_ACCESSIBILITY_FOCUS)
        }
    }

    private fun getFocusableNode(nodeInfo: NodeInfo): NodeInfo? {

        if (NodeValidator.isRequiredFocus(nodeInfo)) {
            return nodeInfo
        }

        val ancestor = nodeInfo.getNearestAncestor(
            NodeValidator::isRequiredFocus
        )

        return ancestor ?: when {
            NodeValidator.isReadable(nodeInfo) -> nodeInfo
            else -> null
        }
    }
}
